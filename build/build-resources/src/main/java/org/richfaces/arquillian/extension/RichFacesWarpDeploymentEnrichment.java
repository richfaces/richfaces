package org.richfaces.arquillian.extension;

import org.jboss.arquillian.container.test.spi.RemoteLoadableExtension;
import org.jboss.arquillian.warp.spi.WarpDeploymentEnrichmentExtension;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.richfaces.arquillian.warp.WarpRequestSynchronizer;

public class RichFacesWarpDeploymentEnrichment implements WarpDeploymentEnrichmentExtension {

    @Override
    public JavaArchive getEnrichmentLibrary() {
        return ShrinkWrap.create(JavaArchive.class, "richfaces-warp-extension.jar")
            .addClass(WarpRequestSynchronizer.class)
            .addClass(RichFacesWarpExtension.class)
            .addAsServiceProvider(RemoteLoadableExtension.class, RichFacesWarpExtension.class);
    }

    @Override
    public void enrichWebArchive(WebArchive webArchive) {
    }

}
