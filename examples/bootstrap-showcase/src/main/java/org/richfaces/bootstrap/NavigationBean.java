package org.richfaces.bootstrap;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

@ManagedBean
@ApplicationScoped
public class NavigationBean implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -1734975574328260134L;
    private HashMap<String,List<String>> menu;

    @PostConstruct
    private void init(){
      menu = new HashMap<>();

      List<String> ajax= new ArrayList<>(Arrays.asList("ajax","commandButton","commandLink","actionListener","jsFunction","poll","push","param"));
      menu.put("Ajax",ajax);

      List<String> ajaxQueue = new ArrayList<>(Arrays.asList("queue","attachQueue"));
      menu.put("Ajax queue",ajaxQueue);

      List<String> ajaxOutput = new ArrayList<>(Arrays.asList("outputPanel","status","region","mediaOutput","log"));
      menu.put("Ajax output", ajaxOutput);


      List<String> output = new LinkedList<>();
      output.add("chart");
      output.add("panel");
      output.add("togglePanel");
      output.add("tabPanel");
      output.add("collapsiblePanel");
      output.add("accordion");
      output.add("popupPanel");
      output.add("progressBar");
      output.add("tooltip");

      menu.put("Output", output);

      List<String> input = new LinkedList<>();
      input.add("autocomplete");
      input.add("calendar");
      input.add("editor");
      input.add("inputNumberSlider");
      input.add("inputNumberSpinner");
      input.add("inplaceInput");
      input.add("fileUpload");
      menu.put("Input", input);

      List<String> selects = new ArrayList<>(Arrays.asList("inplaceSelect","select","orderingList","pickList"));
      menu.put("Select",selects);

      List<String> iteration= new ArrayList<>(Arrays.asList("repeat","dataTable","extendedDataTable","collapsibleSubTable","dataScroller","list","dataGrid"));
      menu.put("Data iteration",iteration);


      List<String> validation= new ArrayList<>(Arrays.asList("graphValidator","message","messages","notify"));
      menu.put("Validation",validation);

      List<String> menus= new ArrayList<>(Arrays.asList("panelMenu","toolbar","contextMenu","dropDownMenu"));
      menu.put("Menus",menus);

      List<String> miscellaneous= new ArrayList<>(Arrays.asList("skinning","placeholder","RichFaces functions","Focus","componentControl","hashParam","hotKey","jQuery"));
      menu.put("Miscellaneous",miscellaneous);

    }

    public HashMap<String, List<String>> getMenu() {
        return menu;
    }

    public List<String> getCategories(){
        List<String> categories = new ArrayList<>(menu.keySet());
        return categories;
    }

    public List<List<String>> getCategoryRows(){

        int maxInRow = 6;

        List<String> categories = new ArrayList<>(menu.keySet());
        List<List<String>> rows = new ArrayList<>();
        for(int i =0; i<categories.size();){
            if((i+maxInRow)<categories.size()){
                rows.add(categories.subList(i, i+maxInRow));
            }
            else{
                rows.add(categories.subList(i, categories.size()));
            }
            i+=maxInRow;

        }

        return rows;

    }
}
