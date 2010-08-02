/**
 * License Agreement.
 *
 *  JBoss RichFaces - Ajax4jsf Component Library
 *
 * Copyright (C) 2007  Exadel, Inc.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License version 2.1 as published by the Free Software Foundation.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301  USA
 */

package org.ajax4jsf.context;

/**
 * @author Nick Belaevski
 */

//TODO remove - https://jira.jboss.org/jira/browse/RFPL-42
public class ViewResources {

//  private static final String INIT_PARAMETER_PREFIX = "_init_parameter_";
//  private static final Object NULL = new Object();
//
//  private String scriptStrategy;
//
//  private String styleStrategy;
//
//  private boolean useStdControlsSkinning;
//
//  private boolean useStdControlsSkinningClasses;
//
//  private HeadResponseWriter componentWriter;
//
//  private HeadResponseWriter userWriter;
//
//  private RenderKit renderKit;
//
//  private Node[] headNodes = null;
//
//  private static final String EXTENDED_SKINNING_ON_NO_SCRIPTS_INFO_KEY = ViewResources.class.getName() + "EXTENDED_SKINNING_ON_NO_SCRIPTS_INFO_KEY";
//  
//  private static final InternetResource EXTENDED_SKINNING_ON_RESOURCE = new InternetResourceBase() {
//      
//      private final String RESOURCE_KEY = this.getClass().getName();
//      
//      @Override
//      public void encode(FacesContext context, Object data)
//              throws IOException {
//
//          encode(context, data, Collections.EMPTY_MAP);
//      }
//      
//      @Override
//      public void encode(FacesContext context, Object data,
//              Map<String, Object> attributes) throws IOException {
//
//          Map<String, Object> requestMap = context.getExternalContext().getRequestMap();
//          if (requestMap.get(RESOURCE_KEY) == null) {
//              ResponseWriter writer = context.getResponseWriter();
//              writer.startElement(HTML.SCRIPT_ELEM, null);
//              
//              if (!attributes.containsKey(HTML.TYPE_ATTR)) {
//                  writer.writeAttribute(HTML.TYPE_ATTR, "text/javascript", null);
//              }
//              
//              for (Map.Entry<String, Object> entry : attributes.entrySet()) {
//                  writer.writeAttribute(entry.getKey(), entry.getValue(), null);
//              }
//              
//              writer.writeText("window.RICH_FACES_EXTENDED_SKINNING_ON=true;", null);
//              
//              writer.endElement(HTML.SCRIPT_ELEM);
//
//              requestMap.put(RESOURCE_KEY, Boolean.TRUE);
//          }
//      }
//  };
//  
//  public void setScriptStrategy(String scriptsStrategy) {
//      this.scriptStrategy = scriptsStrategy;
//  }
//
//  public void setStyleStrategy(String stylesStrategy) {
//      this.styleStrategy = stylesStrategy;
//  }
//
//  public void setUseStdControlsSkinning(boolean stdControlsSkinning) {
//      this.useStdControlsSkinning = stdControlsSkinning;
//  }
//
//  public void setUseStdControlsSkinningClasses(boolean stdControlsSkinningClasses) {
//      this.useStdControlsSkinningClasses = stdControlsSkinningClasses;
//  }
//
//  public void setExtendedSkinningAllowed(boolean extendedSkinningAllowed) {
//      this.extendedSkinningAllowed = extendedSkinningAllowed;
//  }
//
//  private static String SCRIPT = HTML.SCRIPT_ELEM;
//  private static String SCRIPT_UC = SCRIPT.toUpperCase(Locale.US);
//  
//  private static String SRC = HTML.SRC_ATTRIBUTE;
//  private static String SRC_UC = SRC.toUpperCase(Locale.US);
//
//  private void mergeHeadResourceNode(List<Node> nodes, Set<String> renderedScripts, Node node) {
//      boolean shouldAdd = true;
//      
//      String nodeName = node.getNodeName();
//      if (SCRIPT.equals(nodeName) || SCRIPT_UC.equals(nodeName)) {
//          if (node.getFirstChild() == null) {
//              //no text content etc.
//          
//              NamedNodeMap attributes = node.getAttributes();
//              if (attributes != null) {
//                  Node item = attributes.getNamedItem(SRC);
//                  if (item == null) {
//                      attributes.getNamedItem(SRC_UC);
//                  }
//                  
//                  if (item != null) {
//                      String src = item.getNodeValue();
//                      if (src != null) {
//                          if (renderedScripts.contains(src)) {
//                              shouldAdd = false;
//                          } else {
//                              renderedScripts.add(src);
//                          }
//                      }
//                  }
//              }
//          }
//      }
//      
//      if (shouldAdd) {
//          nodes.add(node);
//      }
//  }
//  
//  private Node[] mergeHeadResourceNodes() {
//      List<Node> result = new ArrayList<Node>();
//  
//      Set<String> scripts = new HashSet<String>();
//      
//      for (Node node : componentWriter.getNodes()) {
//          mergeHeadResourceNode(result, scripts, node);
//      }
//      
//      for (Node node : userWriter.getNodes()) {
//          mergeHeadResourceNode(result, scripts, node);
//      }
//      
//      return result.toArray(new Node[result.size()]);
//  }
//  
//  public Node[] getHeadEvents() {
//      if (headNodes == null) {
//          headNodes = mergeHeadResourceNodes();
//      }
//      
//      return headNodes;
//  }
//
//  private static final Log log = LogFactory.getLog(ViewResources.class);
//
//  //todo
//
//  private static final Map<String, Object> EXTENDED_SKINNING = new HashMap<String, Object>(1);
//
//  static {
//      EXTENDED_SKINNING.put(HTML.MEDIA_ATTRIBUTE, "rich-extended-skinning");
//  }
//
//  public static final String SKINNING_STYLES_PATH = "/org/richfaces/renderkit/html/css/";
//
//  public static final String QUEUE_SCRIPT_RESOURCE = "org.ajax4jsf.renderkit.html.scripts.QueueScript";
//
//  private boolean extendedSkinningAllowed;
//
//  private boolean processScripts;
//
//  private boolean processStyles;
//
//  private boolean useSkinning;
//
//  private InternetResourceBuilder resourceBuilder;
//
//  private boolean ajaxRequest;
//
//  public static final String COMPONENT_RESOURCE_LINK_CLASS = "component";
//
//  public static final String USER_RESOURCE_LINK_CLASS = "user";
//
//  class HeadResponseWriter extends SAXResponseWriter {
//
//      public Node[] getNodes() {
//          return ((ResponseWriterContentHandler) getXmlConsumer()).getNodes();
//      }
//
//      public HeadResponseWriter(String linkClass) {
//          super(new ResponseWriterContentHandler(linkClass));
//
//      }
//  }
//
//  private void encodeResources(FacesContext context, ResourceRenderer renderer, Set<String> set) throws IOException {
//
//      if (set != null) {
//          URIInternetResource resourceImpl = new URIInternetResource();
//
//          for (String uri : set) {
//              resourceImpl.setUri(uri);
//              renderer.encode(resourceImpl, context, null);
//          }
//      }
//  }
//
//  private boolean encodeSkinningResources(FacesContext context, InternetResourceBuilder resourceBuilder) throws IOException, FacesException {
//      String resourceSuffix = null;
//
//      if (useStdControlsSkinning) {
//          if (useStdControlsSkinningClasses) {
//              resourceSuffix = "_both.xcss";
//          } else {
//              resourceSuffix = ".xcss";
//          }
//      } else {
//          if (useStdControlsSkinningClasses) {
//              resourceSuffix = "_classes.xcss";
//          } else {
//              //no resources
//          }
//      }
//
//      if (resourceSuffix != null) {
//          resourceBuilder.createResource(
//                  this, SKINNING_STYLES_PATH.concat("basic").concat(resourceSuffix)).encode(context, null);
//
//          if (extendedSkinningAllowed) {
//              resourceBuilder.createResource(
//                      this, SKINNING_STYLES_PATH.concat("extended").concat(resourceSuffix)).encode(context, null, EXTENDED_SKINNING);
//          }
//
//          return true;
//      }
//
//      return false;
//  }
//
//  /**
//   * Find renderer for given component.
//   * 
//   * @param context
//   * @param comp
//   * @param renderKit
//   * @return
//   */
//  private Renderer getRenderer(FacesContext context, UIComponent comp) {
//
//      String rendererType = comp.getRendererType();
//      if (rendererType != null) {
//          return (renderKit.getRenderer(comp.getFamily(), rendererType));
//      } else {
//          return (null);
//      }
//  }
//
//  public void processHeadResources(FacesContext context) throws FacesException {
//
//      RenderKitFactory rkFactory = (RenderKitFactory) FactoryFinder
//      .getFactory(FactoryFinder.RENDER_KIT_FACTORY);
//      renderKit = rkFactory.getRenderKit(context, context
//              .getViewRoot().getRenderKitId());
//
//      boolean scriptsOff = false;
//      
//      processStyles = true;
//      processScripts = true;
//      useSkinning = false;
//
//      ajaxRequest = AjaxContext.getCurrentInstance(context).isAjaxRequest(context);
//
//      if (log.isDebugEnabled()) {
//          log
//          .debug("Process component tree for collect used scripts and styles");
//      }
//
//      String skinStyleSheetUri = null;
//      String skinExtendedStyleSheetUri = null;
//
//      Skin skin = null;
//      try {
//          skin = SkinFactory.getInstance().getSkin(context);
//          // For a "NULL" skin, do not collect components stylesheets
//          if ("false".equals(skin.getParameter(context,
//                  Skin.LOAD_STYLE_SHEETS))) {
//              processStyles = false;
//          }
//          // Set default style sheet for current skin.
//          skinStyleSheetUri = (String) skin.getParameter(context,
//                  Skin.GENERAL_STYLE_SHEET);
//          // Set default style sheet for current skin.
//          skinExtendedStyleSheetUri = (String) skin.getParameter(context,
//                  Skin.EXTENDED_STYLE_SHEET);
//      } catch (SkinNotFoundException e) {
//          if (log.isWarnEnabled()) {
//              log.warn("Current Skin is not found", e);
//          }
//      }
//
//      resourceBuilder = InternetResourceBuilder.getInstance();
//
//      ResponseWriter oldResponseWriter = context.getResponseWriter();
//
//      componentWriter = new HeadResponseWriter("component");
//      userWriter = new HeadResponseWriter("user");
//
//      try {
//          componentWriter.startDocument();
//          userWriter.startDocument();
//
//          context.setResponseWriter(componentWriter);
//
//          // Check init parameters for a resources processing.
//          if (null != scriptStrategy) {
//              if (InternetResourceBuilder.LOAD_NONE
//                      .equals(scriptStrategy)) {
//                  scriptsOff = true;
//                  processScripts = false;
//              } else if (InternetResourceBuilder.LOAD_ALL
//                      .equals(scriptStrategy)) {
//                  processScripts = false;
//                  // For an "ALL" strategy, it is not necessary to load scripts in the ajax request
//                  if (!ajaxRequest) {
//                      try {
//                          resourceBuilder
//                          .createResource(
//                                  this,
//                                  InternetResourceBuilder.COMMON_FRAMEWORK_SCRIPT).encode(context, null);
//                          resourceBuilder
//                          .createResource(
//                                  this,
//                                  InternetResourceBuilder.COMMON_UI_SCRIPT).encode(context, null);
//
//                      } catch (ResourceNotFoundException e) {
//                          if (log.isWarnEnabled()) {
//                              log
//                              .warn("No aggregated javaScript library found "
//                                      + e.getMessage());
//                          }
//                      }
//
//                  }
//              }
//          }
//
//          if (InternetResourceBuilder.LOAD_NONE.equals(styleStrategy)) {
//              processStyles = false;
//          } else if (InternetResourceBuilder.LOAD_ALL
//                  .equals(styleStrategy)) {
//              processStyles = false;
//              // For an "ALL" strategy, it is not necessary to load styles
//              // in the ajax request
//              if (!ajaxRequest) {
//
//                  try {
//                      useSkinning = encodeSkinningResources(context, resourceBuilder);
//
//                      resourceBuilder
//                      .createResource(this, InternetResourceBuilder.COMMON_STYLE).encode(context, null);
//
//                  } catch (ResourceNotFoundException e) {
//                      if (log.isWarnEnabled()) {
//                          log.warn("No stylesheet found "
//                                  + e.getMessage());
//                      }
//                  }
//
//              }
//          } else {
//              useSkinning = encodeSkinningResources(context, resourceBuilder);
//          }
//
//          //traverse components
//          //traverse(context, context.getViewRoot());
//
//          context.setResponseWriter(componentWriter);
//
//          QueueRegistry queueRegistry = QueueRegistry.getInstance(context);
//          if (Boolean.valueOf(getInitParameterValue(context, "org.richfaces.queue.global.enabled"))) {
//              queueRegistry.setShouldCreateDefaultGlobalQueue();
//          }
//          
//          if (queueRegistry.hasQueuesToEncode()) {
//              InternetResource queueScriptResource = resourceBuilder.getResource(QUEUE_SCRIPT_RESOURCE);
//              queueScriptResource.encode(context, null);
//          }
//          
//          // Append Skin StyleSheet after a
//          if (null != skinStyleSheetUri) {
//              String resourceURL = context.getApplication()
//              .getViewHandler().getResourceURL(context,
//                      skinStyleSheetUri);
//
//              URIInternetResource resourceImpl = new URIInternetResource();
//              resourceImpl.setUri(resourceURL);
//              resourceImpl.setRenderer(resourceBuilder.getStyleRenderer());
//              resourceImpl.encode(context, null);
//
//              useSkinning = true;
//          }
//
//          if (null != skinExtendedStyleSheetUri && extendedSkinningAllowed) {
//              String resourceURL = context.getApplication().getViewHandler().getResourceURL(context,
//                      skinExtendedStyleSheetUri);
//
//              URIInternetResource resourceImpl = new URIInternetResource();
//              resourceImpl.setUri(resourceURL);
//              resourceImpl.setRenderer(resourceBuilder.getStyleRenderer());
//              resourceImpl.encode(context, null, EXTENDED_SKINNING);
//
//              useSkinning = true;
//          }
//
//          if (useSkinning && extendedSkinningAllowed) {
//              if (!ajaxRequest) {
//                  if (!scriptsOff) {
//                      //skinning levels aren't dynamic, page-level setting cannot be changed 
//                      //by AJAX request
//                      EXTENDED_SKINNING_ON_RESOURCE.encode(context, null);
//                  } else {
//                      
//                      Map<String, Object> applicationMap = context.getExternalContext().getApplicationMap();
//                      if (applicationMap.get(EXTENDED_SKINNING_ON_NO_SCRIPTS_INFO_KEY) == null) {
//                          //do it once per application life - strategies can be changed dynamically
//                          ResponseWriter writer = context.getResponseWriter();
//                          try {
//                              StringWriter stringWriter = new StringWriter();
//                              
//                              if (oldResponseWriter != null) {
//                                  context.setResponseWriter(oldResponseWriter.cloneWithWriter(stringWriter));
//                              } else {
//                                  context.setResponseWriter(this.renderKit.createResponseWriter(
//                                          stringWriter, "text/html", 
//                                          "US-ASCII"));
//                              }
//                              
//                              
//                              EXTENDED_SKINNING_ON_RESOURCE.encode(context, null);
//
//                              stringWriter.flush();
//                              
//                              if (log.isInfoEnabled()) {
//                                  log.info("Extended skinning is on and NONE scripts loading strategy was detected. " +
//                                          "Do not forget that one of " + InternetResourceBuilder.SKINNING_SCRIPT + " or " + 
//                                          InternetResourceBuilder.COMMON_FRAMEWORK_SCRIPT + " resources should be presented " +
//                                          "on the page together with the following code: \n" + stringWriter.getBuffer().toString() +
//                                          "\nfor extended level of skinning to work.");
//                              }
//                          } finally {
//                              if (writer != null) {
//                                  context.setResponseWriter(writer);
//                              }
//                          }
//
//                          applicationMap.put(EXTENDED_SKINNING_ON_NO_SCRIPTS_INFO_KEY, Boolean.TRUE);
//                      }
//                  }
//              }
//
//              if (processScripts) {
//                  InternetResource resource = resourceBuilder.createResource(null, 
//                      InternetResourceBuilder.SKINNING_SCRIPT);
//
//                  resource.encode(context, null);
//              }
//          }
//
//          componentWriter.endDocument();
//          userWriter.endDocument();
//      } catch (IOException e) {
//          throw new FacesException(e.getLocalizedMessage(), e);
//      } finally {
//          if (oldResponseWriter != null) {
//              context.setResponseWriter(oldResponseWriter);
//          }
//      }
//  }
//
//  private static String getInitParameterValue(FacesContext context, String parameterName) {
//      
//      String key = INIT_PARAMETER_PREFIX + parameterName;
//      
//      ExternalContext externalContext = context.getExternalContext();
//      Map<String, Object> applicationMap = externalContext.getApplicationMap();
//      Object mutex = externalContext.getRequest();
//      Object parameterValue = null;
//      
//      synchronized (mutex) {
//          parameterValue = applicationMap.get(key);
//
//          if (parameterValue == null) {
//
//              String initParameter = externalContext.getInitParameter(parameterName);
//              if (initParameter != null) {
//                  
//                  if (ELUtils.isValueReference(initParameter)) {
//                      Application application = context.getApplication();
//                      ExpressionFactory expressionFactory = application.getExpressionFactory();
//                      
//                      parameterValue = expressionFactory.createValueExpression(context.getELContext(), 
//                              initParameter,
//                              String.class);
//                  } else {
//                      parameterValue = initParameter;
//                  }
//                  
//              } else {
//                  parameterValue = NULL;
//              }
//              
//              applicationMap.put(key, parameterValue);
//          }
//      }
//      
//      return evaluate(context, parameterValue);
//  }
//  
//  private static String evaluate(FacesContext context, Object parameterValue) {
//      if (parameterValue == NULL || parameterValue == null) {
//          return null;
//      } else if (parameterValue instanceof ValueExpression) {
//          ValueExpression expression = (ValueExpression) parameterValue;
//          
//          return (String) expression.getValue(context.getELContext());
//      } else {
//          return parameterValue.toString();
//      }
//  }
//
//  public void initialize(FacesContext context) {
//      boolean extendedSkinningAllowed = true;
//      String skinningLevel = getInitParameterValue(context, InternetResourceBuilder.CONTROL_SKINNING_LEVEL);
//      if (skinningLevel != null && skinningLevel.length() > 0) {
//          if (InternetResourceBuilder.BASIC.equals(skinningLevel)) {
//              extendedSkinningAllowed = false;
//          } else if (!InternetResourceBuilder.EXTENDED.equals(skinningLevel)) {
//              throw new IllegalArgumentException("Value: " + skinningLevel + 
//                      " of " + InternetResourceBuilder.CONTROL_SKINNING_LEVEL 
//                      + " init parameter is invalid! Only " + InternetResourceBuilder.EXTENDED 
//                      + ", " + InternetResourceBuilder.BASIC + " can be used");
//          }
//      }
//      
//      this.setExtendedSkinningAllowed(extendedSkinningAllowed);
//      
//      this.setScriptStrategy(getInitParameterValue(context, 
//              InternetResourceBuilder.LOAD_SCRIPT_STRATEGY_PARAM));
//
//      boolean useStdControlsSkinning = false;
//
//      String stdControlsSkinning = getInitParameterValue(context, InternetResourceBuilder.STD_CONTROLS_SKINNING_PARAM);
//      if (stdControlsSkinning != null) {
//          useStdControlsSkinning = InternetResourceBuilder.ENABLE.equals(stdControlsSkinning);
//      }
//      
//      this.setUseStdControlsSkinning(useStdControlsSkinning);
//
//      boolean useStdControlsSkinningClasses = true;
//
//      String stdControlsSkinningClasses = getInitParameterValue(context, InternetResourceBuilder.STD_CONTROLS_SKINNING_CLASSES_PARAM);
//      if (stdControlsSkinningClasses != null) {
//          useStdControlsSkinningClasses = InternetResourceBuilder.ENABLE.equals(stdControlsSkinningClasses);
//      }
//
//      this.setUseStdControlsSkinningClasses(useStdControlsSkinningClasses);
//      
//      this.setStyleStrategy(getInitParameterValue(context, 
//              InternetResourceBuilder.LOAD_STYLE_STRATEGY_PARAM));
//
//
//  }
//  
}
