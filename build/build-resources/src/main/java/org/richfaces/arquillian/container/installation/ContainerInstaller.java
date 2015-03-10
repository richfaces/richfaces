package org.richfaces.arquillian.container.installation;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.io.FileUtils;
import org.jboss.arquillian.core.api.annotation.Observes;
import org.jboss.arquillian.core.spi.Validate;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.richfaces.arquillian.configuration.FundamentalTestConfiguration;
import org.richfaces.arquillian.configuration.FundamentalTestConfigurationContext;

/**
 * Installs the container distribution and configuration before it will be started
 */
public class ContainerInstaller {

    private Logger log = Logger.getLogger(ContainerInstaller.class.getName());

    private FundamentalTestConfiguration configuration = FundamentalTestConfigurationContext.getProxy();

    public void unpackContainerDistribution(@Observes InstallContainer event) {
        Validate.notNull(configuration, "fundamental test configuration is not setup");

        String distribution = configuration.getContainerDistribution();

        if (distribution == null || distribution.isEmpty()) {
            return;
        }

        File containerHome = new File(configuration.getContainerHome());
        Validate.notNull(containerHome, "container home must be set");

        if (containerHome.exists()) {
            log.info(String.format("The container is already installed in '%s'", containerHome));
            return;
        }

        File unpackDestination = containerHome.getParentFile();
        InputStream inputStream;

        try {
            URL distributionUrl = new URL(distribution);
            log.info(String.format("The container distribution will be resolved from URL '%s'", distribution));
            inputStream = distributionUrl.openStream();
        } catch (MalformedURLException e) {
            log.info(String.format("The container distribution will be resolved from Maven artifact '%s'", distribution));
            inputStream = Maven.resolver().resolve(distribution).withoutTransitivity().asSingleInputStream();
        } catch (IOException e) {
            throw new IllegalStateException("Failed to resolve the container distribution", e);
        }

        unzip(inputStream, unpackDestination, false);

        log.info(String.format("The container distribution '%s' was installed into '%s'", distribution,
                unpackDestination.getAbsolutePath()));

        if (!containerHome.exists()) {
            throw new IllegalStateException(String.format(
                    "The container distribution was unpacked but the containerHome (%s) still doesn't exist", containerHome));
        }

        configuration.setContainerInstalledFromDistribution(true);
    }

    public void unpackContainerConfigurationFiles(@Observes ConfigureContainer event) {
        Validate.notNull(configuration, "fundamental test configuration is not setup");

        String configurationFiles = configuration.getContainerConfiguration();

        if (configurationFiles == null || configurationFiles.isEmpty()) {
            return;
        }

        Validate.notNull(configuration.getContainerHome(), "container home must be set");
        File containerHome = new File(configuration.getContainerHome());

        InputStream artifactStream = Maven.configureResolver().withClassPathResolution(false)
            .resolve(configurationFiles).withoutTransitivity().asSingleInputStream();
        unzip(artifactStream, containerHome, true);

        log.info(String.format("The container configuration '%s' was unpacked into '%s'", configurationFiles,
                containerHome.getAbsolutePath()));
    }

    public void uninstallContainer(@Observes UninstallContainer event) {
        Validate.notNull(configuration, "fundamental test configuration is not setup");

        if (configuration.containerShouldBeUninstalled()) {
            File containerHome = new File(configuration.getContainerHome());

            log.info(String.format("The container will be uninstalled from '%s'", containerHome.getAbsolutePath()));

            if (containerHome.exists()) {
                FileUtils.deleteQuietly(containerHome);
            }
        }
    }

    private void unzip(InputStream inputStream, File destination, boolean overwrite) {
        try {
            byte[] buf = new byte[1024];
            ZipInputStream zipinputstream = null;
            ZipEntry zipentry;
            zipinputstream = new ZipInputStream(inputStream);

            zipentry = zipinputstream.getNextEntry();
            while (zipentry != null) {
                int n;
                FileOutputStream fileoutputstream;
                File newFile = new File(destination, zipentry.getName());
                if (zipentry.isDirectory()) {
                    newFile.mkdirs();
                    zipentry = zipinputstream.getNextEntry();
                    continue;
                }

                if (newFile.exists() && overwrite) {
                    log.info("Overwriting " + newFile);
                    newFile.delete();
                }

                fileoutputstream = new FileOutputStream(newFile);

                while ((n = zipinputstream.read(buf, 0, 1024)) > -1) {
                    fileoutputstream.write(buf, 0, n);
                }

                fileoutputstream.close();
                zipinputstream.closeEntry();
                zipentry = zipinputstream.getNextEntry();

            }

            zipinputstream.close();
        } catch (Exception e) {
            throw new IllegalStateException("Can't unzip input stream", e);
        }
    }
}