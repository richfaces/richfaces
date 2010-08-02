///**
// * License Agreement.
// *
// * Rich Faces - Natural Ajax for Java Server Faces (JSF)
// *
// * Copyright (C) 2007 Exadel, Inc.
// *
// * This library is free software; you can redistribute it and/or
// * modify it under the terms of the GNU Lesser General Public
// * License version 2.1 as published by the Free Software Foundation.
// *
// * This library is distributed in the hope that it will be useful,
// * but WITHOUT ANY WARRANTY; without even the implied warranty of
// * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// * Lesser General Public License for more details.
// *
// * You should have received a copy of the GNU Lesser General Public
// * License along with this library; if not, write to the Free Software
// * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301  USA
// */
//
//package org.richfaces.skin;
//
//import javax.faces.context.FacesContext;
//
//import org.ajax4jsf.tests.AbstractThreadedAjax4JsfTestCase;
//import org.richfaces.skin.Skin;
//import org.richfaces.skin.SkinFactory;
//
///**
// * @author asmirnov@exadel.com (latest modification by $Author: ishabalov $)
// * @version $Revision: 1.1.2.2 $ $Date: 2007/02/20 20:58:11 $
// * 
// */
//public class SkinThreadsTestCase extends AbstractThreadedAjax4JsfTestCase {
//
//  /**
//   * @param s
//   */
//  public SkinThreadsTestCase(String s) {
//      super(s);
//      // TODO Auto-generated constructor stub
//  }
//
//  /*
//   * (non-Javadoc)
//   * 
//   * @see com.exadel.vcp.tests.VcpJsfTestCase#setUp()
//   */
//public void setUp() throws Exception {
//      // TODO Auto-generated method stub
//      super.setUp();
//      servletContext.setAttribute("skin", new  Bean());
//  }
//  /*
//   * (non-Javadoc)
//   * 
//   * @see com.exadel.vcp.tests.VcpJsfTestCase#tearDown()
//   */
//  public void tearDown() throws Exception {
//      // TODO Auto-generated method stub
//      super.tearDown();
//  }
//
//  
//  public class SkinTestRunnable extends TestCaseRunnable {
//
//      
//      /**
//       * 
//       */
//      public SkinTestRunnable() {
//          // TODO Auto-generated constructor stub
//      }
//
//      /* (non-Javadoc)
//       * @see com.exadel.vcp.tests.ThreadedVcpJsfTestCase.TestCaseRunnable#runTestCase(javax.faces.context.FacesContext)
//       */
//      public void runTestCase(FacesContext context) throws Throwable {
//          context.getExternalContext().getRequestMap().put("test", new Bean());
//          Skin skin = SkinFactory.getInstance().getSkin(context);
//          assertNotNull(skin);
//          assertEquals("TEST", skin.getRenderKitId(context));
//          assertEquals("binded.string", skin.getParameter(context, "bind.property"));
//          assertEquals("bindedstring", skin.getParameter(context, "string.property"));
//          assertEquals("10", skin.getParameter(context, "int.property"));
//          assertNull(skin.getParameter(context, "notexist"));
//      }
//      
//  }
//  
//  /**
//   * Test skin factory for thread-safe.
//   */
//  public void testThreadsafe() {
//      TestCaseRunnable[] runnables = new TestCaseRunnable[20];
//      for (int i = 0; i < runnables.length; i++) {
//          runnables[i] = new SkinTestRunnable();
//          
//      }
//      this.runTestCaseRunnables(runnables);
//  }
//  /*
//   * (non-Javadoc)
//   * 
//   * @see com.exadel.vcp.tests.VcpJsfTestCase#getSkinName()
//   */
//  protected String getSkinName() {
//      // TODO Auto-generated method stub
//      return "#{skin.name}";
//  }
//}



