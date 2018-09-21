package library.assistant.ui.main;

import com.jfoenix.controls.*;
import com.jfoenix.transitions.hamburger.HamburgerSlideCloseTransition;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javassist.*;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;
import javassist.bytecode.MethodInfo;
import library.assistant.alert.AlertMaker;
import library.assistant.database.DataHelper;
import library.assistant.database.DatabaseHandler;
import library.assistant.model.Function;
import library.assistant.service.Csv;
import library.assistant.service.Service;
import library.assistant.ui.callback.BookReturnCallback;
import library.assistant.ui.issuedlist.IssuedListController;
import library.assistant.ui.main.toolbar.ToolbarController;
import library.assistant.ui.settings.Preferences;
import library.assistant.util.LibraryAssistantUtil;
import library.assistant.util.jsonUtil;
import library.assistant.util.processCollection;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import static library.assistant.util.processCollection.*;

public class MainController implements Initializable, BookReturnCallback {

    private static final String BOOK_NOT_AVAILABLE = "Not Available";
    private static final String NO_SUCH_BOOK_AVAILABLE = "No Such Book Available";
    private static final String NO_SUCH_MEMBER_AVAILABLE = "No Such Member Available";
    private static final String BOOK_AVAILABLE = "Available";

    private Boolean isReadyForSubmission = false;
    private DatabaseHandler databaseHandler;
    private PieChart bookChart;
    private PieChart memberChart;
    private ObservableList<Function> functionData = FXCollections.observableArrayList();
    private ObservableList<Function> functionLogicData = FXCollections.observableArrayList();


    @FXML
    private HBox book_info;
    @FXML
    private HBox member_info;
    @FXML
    private TextField bookIDInput;
    @FXML
    private Text bookName;
    @FXML
    private Text bookAuthor;
    @FXML
    private Text bookStatus;
    @FXML
    private TextField memberIDInput;
    @FXML
    private Text memberName;
    @FXML
    private Text memberMobile;
    @FXML
    private JFXTextField bookID;
    @FXML
    private StackPane rootPane;
    @FXML
    private JFXHamburger hamburger;
    @FXML
    private JFXDrawer drawer;
    @FXML
    private Text memberNameHolder;
    @FXML
    private Text memberEmailHolder;
    @FXML
    private Text memberContactHolder;
    @FXML
    private Text bookNameHolder;
    @FXML
    private Text bookAuthorHolder;
    @FXML
    private Text bookPublisherHolder;
    @FXML
    private Text issueDateHolder;
    @FXML
    private Text numberDaysHolder;
    @FXML
    private Text fineInfoHolder;
    @FXML
    private AnchorPane rootAnchorPane;
    @FXML
    private JFXButton renewButton;
    @FXML
    private JFXButton submissionButton;
    @FXML
    private HBox submissionDataContainer;
    @FXML
    private Tab bookIssueTab;
    @FXML
    private Tab renewTab;
    @FXML
    private JFXTabPane mainTabPane;
    @FXML
    private JFXButton btnIssue;
    @FXML
    private JFXTextField ExecuteNumDown;
    @FXML
    private JFXTextField ExecuteNumUp;
    @FXML
    private JFXTextField ClassPath;
    @FXML
    private JFXTextField ParaAddr;
    @FXML
    private JFXTextField LogicAddr;
    @FXML
    private JFXTextField CsvAddr;
    @FXML
    private JFXTextField LogFile;
    @FXML
    private SplitPane splitpane;
    @FXML
    private TableView<Function> functionTabel;
    @FXML
    private TableColumn<Function,String> classNameColumn;
    @FXML
    private TableColumn<Function,String> methodNameColumn;
    @FXML
    private TableColumn<Function,String> levelColumn;
    @FXML
    private TableColumn<Function,String> enableNameColumn;
    @FXML
    private TableView<Function> functionTabel2;
    @FXML
    private TableColumn<Function,String> levelColumn2;
    @FXML
    private TableColumn<Function,String> classNameColumn2;
    @FXML
    private TableColumn<Function,String> methodNameColumn2;
    @FXML
    private TableColumn<Function,String> enableNameColumn2;

    public HashMap<TreeItem<String>,Integer> treeItemObjectHashMap = new HashMap<TreeItem<String>,Integer>();
    public HashMap<TreeItem<String>,String> treeItemInputParaBooleanHashMap = new HashMap<TreeItem<String>,String>();
    public HashMap<TreeItem<String>,Boolean> treeItemBooleanHashMap = new HashMap<TreeItem<String>,Boolean>();
    public HashMap<TreeItem<String>,Object> treeItemObjectHashMap1 = new HashMap<TreeItem<String>,Object>();
    public HashMap<Integer,Object> integerObjectHashMap = new HashMap<Integer,Object>();
    @FXML
    private TextField paraContent;
    @FXML
    private TextField paraUserInput;
    @FXML
    private TextField fileAddress;
    @FXML
    private TextField numLeft;
    @FXML
    private TextField numRight;
    @FXML
    private TextField rightCount;
    @FXML
    private TextField inputParam;
    @FXML
    private TextField classAdrField;
    @FXML
    private TextField tableNameField;
    @FXML
    private TextField muiltInputParam;
    @FXML
    private TextField JDBCLogAddr;
    @FXML
    private Label inputType;
    @FXML
    private Label resultType;
    @FXML
    private TextArea contentArea;
    @FXML
    private TextArea result;
    @FXML
    private TextField para;
    @FXML
    private TreeView<String> processResult;
    @FXML
    private JFXListView<String> paraList;


    private Function curFunction;
    private ArrayList<Class> curInputClasses = null;
    private Type Resulttype = null;
    private static ArrayList<ArrayList<String>> listContent = new ArrayList<ArrayList<String>>();
    private static ArrayList<ArrayList<String>> muiltContent = new ArrayList<ArrayList<String>>();
    public static String resultCode = "";
    public TreeItem<String> rootNode;
    int num = 1;
    public MainController(){
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.rootNode = new TreeItem<>("函数调用逻辑");
        rootNode.getChildren().clear();
        rootNode.setExpanded(true);
        databaseHandler = DatabaseHandler.getInstance();
        initDrawer();
        initSettings();
        initComponents();
        showClassAndMethodInTable();
        functionTabel.setItems(functionData);
        functionTabel2.setItems(functionLogicData);
        processResult.setRoot(rootNode);
        functionTabel.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showType(newValue));
    }

    @FXML
    private void addFunction(){
        Function selectedFunction = functionTabel2.getSelectionModel().getSelectedItem();
        TreeItem newFunction =
                new TreeItem<>(num +" "+ selectedFunction.classNameProperty().getValue()+"."+getMethodNameWithoutExtra(selectedFunction.methodNameProperty().getValue())+" 类型:"+selectedFunction.enableNameProperty().getValue());
        ClassPool pool = ClassPool.getDefault();
        CtClass pt = null;
        ArrayList<String> paraTypes = getParaTypeFromMethod(selectedFunction.methodNameProperty().getValue());
        try {
            pt = pool.get(selectedFunction.classNameProperty().getValue());
            CtMethod m1 = pt.getDeclaredMethod(getMethodNameWithoutExtra(selectedFunction.methodNameProperty().getValue()));
            String[] params = getMethodParamNames(m1);
            for(int i = 0 ; i < params.length ; i ++ ){
                TreeItem newPara =
                        new TreeItem<>(paraTypes.get(i)+" "+params[i]);
                newFunction.getChildren().add(newPara);
            }
        } catch (NotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        rootNode.getChildren().add(newFunction);
        num ++ ;
    }


    @FXML
    private void removeFunction(){
        for(int i = 0 ; i < rootNode.getChildren().size() ; i ++ ){
            rootNode.getChildren().get(i).setExpanded(false);
        }
        TreeItem<String> tmp =  processResult.getSelectionModel().getSelectedItem();
        int indexOfRemove =  processResult.getSelectionModel().getSelectedIndex();
        if(rootNode.getChildren().contains(tmp)) {
            for (int i = indexOfRemove; i < rootNode.getChildren().size(); i++) {
                String value = rootNode.getChildren().get(i).getValue();
                String[] tmpStrings = value.split(" ");
                int tmpInt = Integer.valueOf(tmpStrings[0]) - 1;
                rootNode.getChildren().get(i).setValue(String.valueOf(tmpInt) + " " + tmpStrings[1]+ " " +tmpStrings[2]);
            }
            rootNode.getChildren().remove(tmp);
            num--;
            processResult.getSelectionModel().select(indexOfRemove);
            if(treeItemObjectHashMap.containsKey(tmp))
                treeItemObjectHashMap.remove(tmp);
            if(treeItemObjectHashMap1.containsKey(tmp))
                treeItemObjectHashMap1.remove(tmp);
            if(treeItemBooleanHashMap.containsKey(tmp))
                treeItemBooleanHashMap.remove(tmp);
        }
    }


    @FXML
    private void upFunction(){
        for(int i = 0 ; i < rootNode.getChildren().size() ; i ++ ){
            rootNode.getChildren().get(i).setExpanded(false);
        }
        TreeItem<String> tmp =  processResult.getSelectionModel().getSelectedItem();
        int indexOfUp =  processResult.getSelectionModel().getSelectedIndex();
        if(indexOfUp != 1 && rootNode.getChildren().contains(tmp)){
            TreeItem<String> upItem = rootNode.getChildren().get(indexOfUp - 2);
            String[] curStrings = tmp.getValue().split(" ");
            String[] upStrings = upItem.getValue().split(" ");
            upItem.setValue(curStrings[0] + " " +upStrings[1]+" "+upStrings[2]);
            tmp.setValue(upStrings[0] + " " +curStrings[1]+" "+curStrings[2]);
            rootNode.getChildren().set(indexOfUp - 2,tmp);
            rootNode.getChildren().set(indexOfUp - 1,upItem);
            processResult.getSelectionModel().select(indexOfUp - 1);
        }
    }
    @FXML
    private void downFunction(){
        for(int i = 0 ; i < rootNode.getChildren().size() ; i ++ ){
            rootNode.getChildren().get(i).setExpanded(false);
        }
        TreeItem<String> tmp =  processResult.getSelectionModel().getSelectedItem();
        int indexOfDown =  processResult.getSelectionModel().getSelectedIndex();
        if(indexOfDown != rootNode.getChildren().size() && rootNode.getChildren().contains(tmp)){
            TreeItem<String> downItem = rootNode.getChildren().get(indexOfDown);
            String[] curStrings = tmp.getValue().split(" ");
            String[] downStrings = downItem.getValue().split(" ");
            downItem.setValue(curStrings[0] + " " +downStrings[1]+" "+downStrings[2]);
            tmp.setValue(downStrings[0] + " " +curStrings[1]+" "+curStrings[2]);
            rootNode.getChildren().set(indexOfDown,tmp);
            rootNode.getChildren().set(indexOfDown - 1,downItem);
            processResult.getSelectionModel().select(indexOfDown + 1);
        }
    }


    @FXML
    private void infoFunction(){
        if(processResult.getSelectionModel().getSelectedItem().getParent().equals(rootNode)){
            TreeItem<String> indexNode =  processResult.getSelectionModel().getSelectedItem();
            int indexOfInfo = 0;
            for(indexOfInfo = 0 ; rootNode.getChildren().get(indexOfInfo) != indexNode ; indexOfInfo ++);
            ObservableList<String> items =FXCollections.observableArrayList ();
            for(int i = 0 ; i < indexOfInfo ; i ++){
                String[] typeStrings = rootNode.getChildren().get(i).getValue().split(" ");
                String typeName = typeStrings[2].substring(3);
                if(!typeName.equals("void")){
                    items.add(typeStrings[0]+" "+ typeStrings[1] + typeStrings[2]);
                }
            }
            paraList.setItems(items);
        }
    }
    @FXML
    private void addParaFunction(){
        TreeItem<String> tmp =  processResult.getSelectionModel().getSelectedItem();
        if(rootNode.getChildren().contains(tmp.getParent())){
            String[] nums = paraList.getSelectionModel().getSelectedItem().split(" ");
            int funNum = Integer.valueOf(nums[0]);
            if(funNum < Integer.valueOf(tmp.getParent().getValue().split(" ")[0])){
                treeItemObjectHashMap.put(tmp,funNum);
                tmp.setValue(tmp.getValue().split("->")[0]+"->"+nums[0]+nums[1]);
                treeItemBooleanHashMap.put(tmp,true);
            }
        }
    }

    @FXML
    private void addParaFromInput(){
        TreeItem<String> tmp =  processResult.getSelectionModel().getSelectedItem();
        if(rootNode.getChildren().contains(tmp.getParent())){
            String paraName = paraUserInput.getText();
            tmp.setValue(tmp.getValue().split("->")[0]+"->paraName:"+paraName);
            treeItemInputParaBooleanHashMap.put(tmp,paraName);

        }
    }
    @FXML
    private void addPara(){
        TreeItem<String> tmp =  processResult.getSelectionModel().getSelectedItem();
        if(rootNode.getChildren().contains(tmp.getParent())){
            String para = paraContent.getText();
            JSONArray jsonArray = jsonUtil.getJsonArrayFromString(para);
            Class tmpClass;
            if(tmp.getValue().contains("[ ]")){
                tmpClass = getClassFromName(tmp.getValue().split("\\[")[0]+"[ ]");
            }
            else{
                tmpClass = getClassFromName(tmp.getValue().split(" ")[0]);
            }

            Object tmpObj;
            if(judgeBasicTypeByName(tmpClass)){
                tmpObj = processCollection.getObjectFromStringAndClass(tmpClass, jsonArray.getString(0));
            }
            else{
                JSONObject tmpStr = (JSONObject)jsonArray.get(0);
                tmpObj = (Object)JSONObject.toBean(tmpStr, tmpClass);
            }
            treeItemObjectHashMap1.put(tmp,tmpObj);
            tmp.setValue(tmp.getValue().split("->")[0]+"->"+para);
            treeItemBooleanHashMap.put(tmp,false);
        }
    }
    private void showClassAndMethodInTable(){
        classNameColumn.setCellValueFactory(
                cellData -> cellData.getValue().classNameProperty());
        methodNameColumn.setCellValueFactory(
                cellData -> cellData.getValue().methodNameProperty());
        levelColumn.setCellValueFactory(
                cellData -> cellData.getValue().levelNameProperty());
        enableNameColumn.setCellValueFactory(
                cellData -> cellData.getValue().enableNameProperty());
        classNameColumn2.setCellValueFactory(
                cellData -> cellData.getValue().classNameProperty());
        methodNameColumn2.setCellValueFactory(
                cellData -> cellData.getValue().methodNameProperty());
        levelColumn2.setCellValueFactory(
                cellData -> cellData.getValue().levelNameProperty());
        enableNameColumn2.setCellValueFactory(
                cellData -> cellData.getValue().enableNameProperty());
    }
    private String showType(Function function){
        String result = "";
        curFunction = function;
        StringProperty className = function.classNameProperty();
        StringProperty methodName = function.methodNameProperty();
        System.out.println(String.valueOf(methodName));
        ArrayList<String> paras =  processCollection.getParaTypeFromMethod(String.valueOf(methodName));

        try{
            String newTypeLabel = "";
            for(String para : paras){
                newTypeLabel += para;
                newTypeLabel += "/";
            }
            inputType.setText(newTypeLabel);
        }
        catch (Exception e){
            System.out.println(e);
        }
        ArrayList<Class> tempClasses = new ArrayList<Class>();
        for(int i = 0 ; i < paras.size() ; i ++){
            tempClasses.add(getClassFromName(paras.get(i)));
        }
        curInputClasses = tempClasses;
        try {
            Class claz = Class.forName(processCollection.stringPropertyToString(className));
            boolean isAbs = Modifier.isAbstract(claz.getModifiers()) ;
            if(isAbs){
                //resultType.setText("abstract class");
                result = "abstract class";
            }
            else{
                Type type = processCollection.judgeClass(processCollection.stringPropertyToString(className),
                        processCollection.stringPropertyToString(methodName),tempClasses);
                //resultType.setText(type.getTypeName());
                Resulttype = type;
                result = type.getTypeName();
            }
        } catch (Exception e) {
            e.printStackTrace();
            //resultType.setText("can't execute");
            result = "can't execute";
        }
        ClassPool pool  =  ClassPool.getDefault();
        CtClass cc  = null;
        try {
            para.setPromptText("[]");
            cc = pool.get(processCollection.stringPropertyToString(className));
            CtMethod cm  =  cc.getDeclaredMethod(getMethodNameWithoutExtra(processCollection.stringPropertyToString(methodName)));
            MethodInfo methodInfo  =  cm.getMethodInfo();
            CodeAttribute codeAttribute  =  methodInfo.getCodeAttribute();
            if(codeAttribute != null){
                LocalVariableAttribute attr  =  (LocalVariableAttribute) codeAttribute.getAttribute(LocalVariableAttribute.tag);
                String[] paramNames  =   new  String[cm.getParameterTypes().length];
                int pos = Modifier.isStatic(cm.getModifiers()) ? 0 : 1;
                if(attr != null){
                    for  ( int  i  =   0 ; i  <  paramNames.length; i ++ )
                        paramNames[i]  =  attr.variableName(i+pos);
                    String content = "";
                    for  ( int  i  =   0 ; i  <  paramNames.length; i ++ ) {
                        content += (paramNames[i]+"/");
                    }
                    para.setPromptText("["+content+"]");
                }
            }
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }

    @FXML
    private void handleAnalysis() {
        resultCode = "";
        ArrayList<String> tmpmidClassAndFunction = new ArrayList<String>();
        ArrayList<String> tmpClassAndFunction = new ArrayList<String>();
        try{
            functionData.clear();
            if (isAnalysisInputValid()) {
                Service service = new Service();
                tmpmidClassAndFunction = service.
                        getFunctionList(Integer.parseInt(ExecuteNumDown.getText()),Integer.parseInt(ExecuteNumUp.getText()),LogFile.getText());
                service.reverseList(tmpmidClassAndFunction,tmpClassAndFunction,1,0);
                ArrayList<String> classAndFunction = new  ArrayList<String>();
                for (String cd:tmpClassAndFunction) {
                    if(!classAndFunction.contains(cd)){
                        classAndFunction.add(cd);
                    }
                }
                int listNum = (classAndFunction.size());//%Integer.parseInt(num.getText()) == 0)?
                //classAndFunction.size()/Integer.parseInt(num.getText()):classAndFunction.size();

                for(int i = 0 ; i < listNum; i ++){
                    String className ;
                    String methodName ;
                    String levelName ;
                    ArrayList<String> splitResult = processCollection.splitClassAndMethodAndLevel(classAndFunction.get(i));
                    className = splitResult.get(0);
                    methodName = splitResult.get(1);
                    levelName = splitResult.get(2);
                    String enable = "";
                    Function tempfunction = new Function(className,methodName,levelName);
                    try{ enable = showType(tempfunction);}
                    catch(Exception e){
                        enable = "error";
                    }
                    finally{
                        tempfunction.setEnableName(enable);
                        functionData.add(tempfunction);
                    }
                }
            }
            if(tmpClassAndFunction.isEmpty()){
                if(resultCode.length() == 0){
                    resultCode = "无法读取";
                }
                AlertMaker.showSimpleAlert("失败",resultCode);
            }else{
                AlertMaker.showSimpleAlert("成功","成功");
            }

        }
        catch (Exception e){
            AlertMaker.showSimpleAlert("失败","失败");
        }
    }

    @FXML
    private void handleLogicAnalysis() {
        resultCode = "";
        ArrayList<String> tmpmidClassAndFunction = new ArrayList<String>();
        ArrayList<String> tmpClassAndFunction = new ArrayList<String>();
        try{
            functionData.clear();
            if (isAnalysisInputValid()) {
                Service service = new Service();
                tmpmidClassAndFunction = service.
                        getFunctionList(Integer.parseInt(ExecuteNumDown.getText()),Integer.parseInt(ExecuteNumUp.getText()),LogFile.getText());
                service.reverseList(tmpmidClassAndFunction,tmpClassAndFunction,1,0);
                ArrayList<String> classAndFunction = new  ArrayList<String>();
                for (String cd:tmpClassAndFunction) {
                    if(!classAndFunction.contains(cd)){
                        classAndFunction.add(cd);
                    }
                }
                int listNum = (classAndFunction.size());//%Integer.parseInt(num.getText()) == 0)?
                //classAndFunction.size()/Integer.parseInt(num.getText()):classAndFunction.size();

                for(int i = 0 ; i < listNum; i ++){
                    String className ;
                    String methodName ;
                    String levelName ;
                    ArrayList<String> splitResult = processCollection.splitClassAndMethodAndLevel(classAndFunction.get(i));
                    className = splitResult.get(0);
                    methodName = splitResult.get(1);
                    levelName = splitResult.get(2);
                    String enable = "";
                    Function tempfunction = new Function(className,methodName,levelName);
                    try{ enable = showType(tempfunction);}
                    catch(Exception e){
                        enable = "error";
                    }
                    finally{
                        tempfunction.setEnableName(enable);
                        functionLogicData.add(tempfunction);
                    }
                }
            }
            if(tmpClassAndFunction.isEmpty()){
                if(resultCode.length() == 0){
                    resultCode = "无法读取";
                }
                AlertMaker.showSimpleAlert("失败",resultCode);
            }else{
                AlertMaker.showSimpleAlert("成功","成功");
            }

        }
        catch (Exception e){
            AlertMaker.showSimpleAlert("失败","失败");
        }
    }

    @FXML
    private void handleGetClassFile(){
        ClassPool pool = ClassPool.getDefault();
        pool.importPackage("javax.swing");
        pool.importPackage("java.lang.reflect.Field");
        pool.importPackage("java.lang.reflect.Method");
        String className = curFunction.classNameProperty().getValue();
        // String packageName = getClassFromName(className).getPackage().getName();
        String packageName = getClassFromName(className).getProtectionDomain().getCodeSource().getLocation().getPath();
        System.out.println(packageName);
        String methodName = getMethodNameWithoutExtra(curFunction.methodNameProperty().getValue());
        CtClass pt = null;
        try {
            pt = pool.get(className);
            CtMethod m1 = pt.getDeclaredMethod(methodName);
            m1.setModifiers(Modifier.PUBLIC);
            CtMethod m2 = CtNewMethod.make("public static boolean export(String methodName , String params, String returnType,String Addr){" +
                    "boolean isSucess=false;" +
                    " java.io.FileOutputStream out=null;" +
                    " java.io.OutputStreamWriter osw=null;" +
                    " java.io.BufferedWriter bw=null;" +
                    "        try {" +
                    "            out = new java.io.FileOutputStream(Addr,true);" +
                    "            osw = new java.io.OutputStreamWriter(out,\"UTF-8\");" +
                    "            bw =new java.io.BufferedWriter(osw);" +
                    "            if(methodName!=null && params != null  && returnType != null){" +
                    "            String a =  methodName +\" \"+ params +\" \"+ returnType ;  bw.append(a).append(\"\\n\");" +
                    "            }" +
                    "            isSucess=true;" +
                    "} catch (Exception e) {" +
                    "            isSucess=false;" +
                    "        }finally{" +
                    "            if(bw!=null){" +
                    "                try {" +
                    "                    bw.close();" +
                    "                    bw=null;" +
                    "                } catch (java.io.IOException e) {" +
                    "                    e.printStackTrace();" +
                    "                } " +
                    "            }" +
                    "            if(osw!=null){" +
                    "                try {" +
                    "                    osw.close();" +
                    "                    osw=null;" +
                    "                } catch (java.io.IOException e) {" +
                    "                    e.printStackTrace();" +
                    "                } " +
                    "            }" +
                    "            if(out!=null){" +
                    "                try {" +
                    "                    out.close();" +
                    "                    out=null;" +
                    "                } catch (java.io.IOException e) {" +
                    "                    e.printStackTrace();" +
                    "                } " +
                    "            }" +
                    "        }" +
                    "        return isSucess;" +
                    "    }", pt);
            pt.addMethod(m2);
            CtMethod m3 = CtNewMethod.make("public static boolean export1(String params, String returnType,String Addr){" +
                    "boolean isSucess=false;" +
                    " java.io.FileOutputStream out=null;" +
                    " java.io.OutputStreamWriter osw=null;" +
                    " java.io.BufferedWriter bw=null;" +
                    "        try {" +
                    "            out = new java.io.FileOutputStream(Addr,true);" +
                    "            osw = new java.io.OutputStreamWriter(out,\"UTF-8\");" +
                    "            bw =new java.io.BufferedWriter(osw);" +
                    "            if( params != null  && returnType != null){" +
                    "            String a = params +\",\"+ returnType ;  bw.append(a).append(\"\\n\");" +
                    "            }" +
                    "            isSucess=true;" +
                    "} catch (Exception e) {" +
                    "            isSucess=false;" +
                    "        }finally{" +
                    "            if(bw!=null){" +
                    "                try {" +
                    "                    bw.close();" +
                    "                    bw=null;" +
                    "                } catch (java.io.IOException e) {" +
                    "                    e.printStackTrace();" +
                    "                } " +
                    "            }" +
                    "            if(osw!=null){" +
                    "                try {" +
                    "                    osw.close();" +
                    "                    osw=null;" +
                    "                } catch (java.io.IOException e) {" +
                    "                    e.printStackTrace();" +
                    "                } " +
                    "            }" +
                    "            if(out!=null){" +
                    "                try {" +
                    "                    out.close();" +
                    "                    out=null;" +
                    "                } catch (java.io.IOException e) {" +
                    "                    e.printStackTrace();" +
                    "                } " +
                    "            }" +
                    "        }" +
                    "        return isSucess;" +
                    "    }", pt);
            pt.addMethod(m3);
            CtMethod m4 = CtNewMethod.make("public static int judgeBasicType(Object obj){" +
                    "        Class cls = obj.getClass();" +
                    "        if(cls == (int.class))" +
                    "            return 1;" +
                    "        else if(cls == (boolean.class))" +
                    "            return 1;" +
                    "        else if(cls == char.class)" +
                    "            return 1;" +
                    "        else if(cls == byte.class)" +
                    "            return 1;" +
                    "        else if(cls == short.class)" +
                    "            return 1;" +
                    "        else if(cls == long.class)" +
                    "            return 1;" +
                    "        else if(cls == double.class)" +
                    "            return 1;" +
                    "        else if(cls == float.class)" +
                    "            return 1;" +
                    "        else if(cls == java.lang.String.class)" +
                    "            return 1;" +
                    "        else if(cls == java.lang.Boolean.class)" +
                    "            return 1;" +
                    "        else if(cls == java.lang.Byte.class)" +
                    "            return 1;" +
                    "        else if(cls == java.lang.Character.class)" +
                    "            return 1;" +
                    "        else if(cls == java.lang.CharSequence.class)" +
                    "            return 1;" +
                    "        else if(cls == java.lang.Double.class)" +
                    "            return 1;" +
                    "        else if(cls == java.lang.Float.class)" +
                    "            return 1;" +
                    "        else if(cls == java.lang.Integer.class)" +
                    "            return 1;" +
                    "        else if(cls == java.lang.Long.class)" +
                    "            return 1;" +
                    "        else if(cls == java.lang.Number.class)" +
                    "            return 1;" +
                    "        else if(cls == java.lang.Short.class)" +
                    "            return 1;" +
                    "        else" +
                    "            return 0;" +
                    "    }", pt);
            pt.addMethod(m4);
            CtMethod m5 = CtNewMethod.make("private static String printFieldMessage(Object object) {" +
                    "        Class class1 = object.getClass();" +
                    "        Field[] fs = class1.getDeclaredFields();" +
                    "        String result = \"\";"+
                    "        for (int i = 0 ; i < fs.length ; i ++) {" +
                    "            fs[i].setAccessible(true);" +
                    "            Class filedType = fs[i].getType();" +
                    "            String typeName = filedType.getName();" +
                    "            String fieldName = fs[i].getName();" +
                    "            result += (typeName + \" \" + fieldName + \":\"+ fs[i].get(object)+ \",\");" +
                    "        }" +
                    "    return result;" +
                    "}", pt);
            pt.addMethod(m5);
            StringBuffer sb = new StringBuffer();
            String address = ParaAddr.getText();
            sb.append("for(int i = 0 ; i < "+curInputClasses.size()+" ; i++) {");
            // sb.append("for(int j = 0 ; j < $args[i].length ; j++){");
            sb.append("if( $args[i].getClass().isArray() ) {");
            sb.append("System.out.println(\"abc\");");
            sb.append("export(\""+"array:"+"\",java.util.Arrays.toString((Object[])$args[i]),\""+methodName+"\",\""+address+"\");");
            sb.append("}");
            sb.append("else{");
            sb.append("if(judgeBasicType($args[i]) == 1)");
            sb.append("export1($args[i].toString(),\""+methodName+"\",\""+address+"\");");
            sb.append("else {export1(printFieldMessage($args[i]),\""+methodName+"\",\""+address+"\");}");
            sb.append("}");
            // sb.append("}");
            sb.append("}");
            String code = sb.toString();
            m1.insertBefore(code);
            pt.writeFile(ClassPath.getText());
            pt.defrost();
        } catch (NotFoundException e) {
            e.printStackTrace();
        } catch (CannotCompileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        AlertMaker.showSimpleAlert("成功","jar包来源："+packageName);
        // CtMethod m = CtNewMethod.make("public void xmove(int dx) {System.out.println(\"sdss\");}", pt);
        // pt.addMethod(m);
        //pt.writeFile();
//            Class c = pt.toClass();
//            TestBean bean = (TestBean) c.newInstance();
//            bean.aaa(1,2,3);
    }

    @FXML
    private void handleShowInfo(){
        contentArea.clear();
        String path = ParaAddr.getText();
        Csv csv = new Csv();
        ArrayList<String> arrays = csv.readLog(path,false);

        for(int i = 0 ; i < arrays.size() ; i ++){
            contentArea.appendText(arrays.get(i)+"\n");
        }
    }

    @FXML
    private void handleExecute() {
        result.clear();
        ArrayList<Object> objectArray = new ArrayList<Object>();
        Object[] objectFinalArray;
        if (para.getText() != null && para.getText().length() != 0) {
            String inputParams = para.getText();
            JSONArray jsonArray = jsonUtil.getJsonArrayFromString(inputParams);
            for (int i = 0; i < curInputClasses.size(); i++) {
                if(judgeBasicTypeByName(curInputClasses.get(i))){
                    Object object = processCollection.getObjectFromStringAndClass(curInputClasses.get(i), jsonArray.getString(i));
                    objectArray.add(object);
                }
                else{
                    JSONObject tmp = (JSONObject)jsonArray.get(i);
                    Class cur = curInputClasses.get(i);
                    Object obj=(Object)JSONObject.toBean(tmp, cur);
                    objectArray.add(obj);
//                    System.out.println(obj);
//                    Field[] fields = cur.getDeclaredFields();
//                    for(int j = 0 ; j < fields.length ; j ++){
//                        fields[j].setAccessible(true);
//                        String filedName = fields[j].getName();
//                        Class filedType = fields[j].getType();
//                        Object tmpValue = tmp.get(filedName);
//                    }
                }
            }
        }
        objectFinalArray = (Object[]) objectArray.toArray();
        Class[] classArray = (Class[])curInputClasses.toArray(new Class[curInputClasses.size()]);
        Object resultText = null;
        try {
            resultText  = processCollection.execute(curFunction.classNameProperty().getValue(),
                    curFunction.methodNameProperty().getValue(),classArray,objectFinalArray);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Collection collection = setItems(resultText);
        if(collection != null){
            Iterator it = collection.iterator();
            while(it.hasNext()){
                listContent.add(new ArrayList<String>());
                Object obj = it.next();
                Collection collection1 = setItems(obj);
                if(collection1 != null){
                    ArrayList<Object> tmp = new ArrayList<Object>(collection1);
                    for(int i=0;i<tmp.size();i++) {
                        Object value = tmp.get(i);
                        if(processCollection.judgeBasicType(value)){
                            result.appendText(value.toString()+" ");
                            listContent.get(listContent.size()-1).add(value.toString());
                        }
                        else{
                            result.appendText(ReflectionToStringBuilder.toString(value)+" ");
                            listContent.get(listContent.size()-1).add(ReflectionToStringBuilder.toString(value));
                        }
                    }
                    result.appendText("\n");
                }
                else{
                    listContent.add(new ArrayList<String>());
                    if(processCollection.judgeBasicType(obj)){
                        result.appendText(obj.toString()+" ");
                        listContent.get(listContent.size()-1).add(obj.toString());
                    }
                    else{
                        result.appendText(ReflectionToStringBuilder.toString(obj)+" ");
                        listContent.get(listContent.size()-1).add(ReflectionToStringBuilder.toString(obj));
                    }
                    result.appendText("\n");
                }
//                    String[] tmp1 = (String[])it.next();
            }
        }
        else{

            listContent.add(new ArrayList<String>());
            if(processCollection.judgeBasicType(resultText)){
                result.appendText(resultText.toString()+" ");
                listContent.get(listContent.size()-1).add(resultText.toString());
            }
            else{
                if(curFunction.enableNameProperty().getValue().equals("java.sql.ResultSet")){
                    ResultSet tmp = (ResultSet)resultText;
                    try {
                        List list = new ArrayList();
                        ResultSetMetaData md = tmp.getMetaData();
                        int columnCount = md.getColumnCount();
                        while (tmp.next()) {
                            for (int i = 1; i <= columnCount; i++) {
                                String key = md.getColumnName(i);
                                String value;
                                if(tmp.getObject(i)!=null){
                                     value = tmp.getObject(i).toString();
                                }else {
                                     value = "";
                                }
                                result.appendText(key +":"+ value +" ");//获取键名及值
                            }
                            result.appendText("\n ");
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

//                    rs.beforeFirst();
//                    String data[][] = new String[row][col];
//                    //取结果集中的数据, 放在data数组中
//                    for (int i = 0; i < row; i++) {
//                        rs.next();
//                        for (int j = 0; j < col; j++) {
//                            data[i][j] = rs.getString (j + 1);
//                            //System.out.println (data[i][j]);
//                        }
//                    }//End for
                }
                else{
                    result.appendText(ReflectionToStringBuilder.toString(resultText)+" ");
                    listContent.get(listContent.size()-1).add(ReflectionToStringBuilder.toString(resultText));
                }
            }

            result.appendText("\n");
        }
    }

    private boolean isAnalysisInputValid() {
        String errorMessage = "";
        if (LogFile.getText() == null || LogFile.getText().length() == 0) {
            errorMessage += "No valid fileAddress!\n";
        }
        if (ExecuteNumDown.getText() == null || ExecuteNumDown.getText().length() <= 0||ExecuteNumDown.getText().length() >=5
                || !processCollection.isNumeric(ExecuteNumDown.getText())) {
            errorMessage += "No valid num!\n";
        }
        if (ExecuteNumUp.getText() == null || ExecuteNumUp.getText().length() <= 0||ExecuteNumUp.getText().length() >=5
                || !processCollection.isNumeric(ExecuteNumUp.getText())) {
            errorMessage += "No valid num!\n";
        }
        if (errorMessage.length() == 0) {
            return true;
        } else {
            System.out.println(errorMessage);
        }
        return false;
    }
    private Stage getStage() {
        return (Stage) rootPane.getScene().getWindow();
    }

    @FXML
    private void handleMenuClose(ActionEvent event) {
        getStage().close();
    }

    @FXML
    private void handleMenuAddBook(ActionEvent event) {
        LibraryAssistantUtil.loadWindow(getClass().getResource("/library/assistant/ui/addbook/add_book.fxml"), "Add New Book", null);
    }

    @FXML
    private void handleMenuAddMember(ActionEvent event) {
        LibraryAssistantUtil.loadWindow(getClass().getResource("/library/assistant/ui/addmember/member_add.fxml"), "Add New Member", null);
    }

    @FXML
    private void handleMenuViewBook(ActionEvent event) {
        LibraryAssistantUtil.loadWindow(getClass().getResource("/library/assistant/ui/listbook/book_list.fxml"), "Book List", null);
    }

    @FXML
    private void handleAboutMenu(ActionEvent event) {
        LibraryAssistantUtil.loadWindow(getClass().getResource("/library/assistant/ui/about/about.fxml"), "About Me", null);
    }

    @FXML
    private void handleMenuSettings(ActionEvent event) {
        LibraryAssistantUtil.loadWindow(getClass().getResource("/library/assistant/ui/settings/settings.fxml"), "Settings", null);
    }

    @FXML
    private void handleMenuViewMemberList(ActionEvent event) {
        LibraryAssistantUtil.loadWindow(getClass().getResource("/library/assistant/ui/listmember/member_list.fxml"), "Member List", null);
    }

    @FXML
    private void handleIssuedList(ActionEvent event) {
        Object controller = LibraryAssistantUtil.loadWindow(getClass().getResource("/library/assistant/ui/issuedlist/issued_list.fxml"), "Issued Book List", null);
        if (controller != null) {
            IssuedListController cont = (IssuedListController) controller;
            cont.setBookReturnCallback(this);
        }
    }

    @FXML
    private void handleMenuFullScreen(ActionEvent event) {
        Stage stage = getStage();
        stage.setFullScreen(!stage.isFullScreen());
    }

    private void initDrawer() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/library/assistant/ui/main/toolbar/toolbar.fxml"));
            VBox toolbar = loader.load();
            drawer.setSidePane(toolbar);
            ToolbarController controller = loader.getController();
            controller.setBookReturnCallback(this);
        } catch (IOException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
        HamburgerSlideCloseTransition task = new HamburgerSlideCloseTransition(hamburger);
        task.setRate(-1);
        hamburger.addEventHandler(MouseEvent.MOUSE_CLICKED, (Event event) -> {
            drawer.toggle();
        });
        drawer.setOnDrawerOpening((event) -> {
            task.setRate(task.getRate() * -1);
            task.play();
            drawer.toFront();
        });
        drawer.setOnDrawerClosed((event) -> {
            drawer.toBack();
            task.setRate(task.getRate() * -1);
            task.play();
        });
    }

    private void clearEntries() {
        memberNameHolder.setText("");
        memberEmailHolder.setText("");
        memberContactHolder.setText("");

        bookNameHolder.setText("");
        bookAuthorHolder.setText("");
        bookPublisherHolder.setText("");

        issueDateHolder.setText("");
        numberDaysHolder.setText("");
        fineInfoHolder.setText("");

        disableEnableControls(false);
        submissionDataContainer.setOpacity(0);
    }

    private void disableEnableControls(Boolean enableFlag) {
        if (enableFlag) {
            renewButton.setDisable(false);
            submissionButton.setDisable(false);
        } else {
            renewButton.setDisable(true);
            submissionButton.setDisable(true);
        }
    }


    @FXML
    private void csvFunction(){
        List<String> csvs = new ArrayList<String>();
        for(int i = 0 ; i < rootNode.getChildren().size() ; i ++) {
            String content = rootNode.getChildren().get(i).getValue();
            String[] strings = content.split(" ");
            String classAndMethod = strings[1];
            String resultType = strings[2];
            resultType = resultType.split(":")[1];
            String[] splitClassAndMethods = classAndMethod.split("\\.");
            String className = "";
            String methodName = "";
            for (int j = 0; j < splitClassAndMethods.length - 1; j++) {
                className += (splitClassAndMethods[j] + ".");
            }
            methodName = splitClassAndMethods[splitClassAndMethods.length - 1];
            className = className.substring(0, className.length() - 1);

            List paras = rootNode.getChildren().get(i).getChildren();
            int parasNum = paras.size();

            String paraType = "";
            String paraContent = "";
            for(int k = 0 ; k < paras.size(); k ++){
                String singlePara = (String) ((TreeItem) paras.get(k)).getValue();
                String typeName = null;
                if(singlePara.contains("[ ]")){  typeName = (singlePara.split(" ")[0]+"]");}

                else{
                    typeName = singlePara.split(" ")[0];
                }
                paraType += (typeName+";");
                TreeItem z =(TreeItem) paras.get(k);

                if(treeItemInputParaBooleanHashMap.containsKey(z)){
                    paraType += "3;";
                    String paraName = treeItemInputParaBooleanHashMap.get(z);
                    paraContent += (paraName+";");
                }
                else{
                    if(treeItemBooleanHashMap.get(z)){
                        paraType += "2;";
                        int paraFunction = treeItemObjectHashMap.get(z);
                        paraContent += (paraFunction+";");
                    }
                    else{
                        paraType += "1;";
                        String paracontent = singlePara.split("->")[1];
                        paraContent += (paracontent+";");
                    }
                }

            }
            if(paras.size() != 0){
                paraType = paraType.substring(0,paraType.length()-1);
                paraContent = paraContent.substring(0,paraContent.length()-1);
            }
            String func = className+";"+methodName+";"+parasNum+";"+paraType+";"+paraContent+";"+resultType;
            csvs.add(func);
        }
        boolean isSucess=false;
        java.io.FileOutputStream out=null;
        java.io.OutputStreamWriter osw=null;
        java.io.BufferedWriter bw=null;
        try {
            out = new java.io.FileOutputStream(CsvAddr.getText(),true);
            osw = new java.io.OutputStreamWriter(out,"utf-8");
            bw =new java.io.BufferedWriter(osw);

            for(int i = 0 ; i < csvs.size() ; i ++){
                bw.append(csvs.get(i)).append("\n");
            }

            isSucess=true;

        } catch (Exception e) {
            isSucess=false;
        }finally{
            if(bw!=null){
                try {
                    bw.close();
                    bw=null;
                } catch (java.io.IOException e) {
                    e.printStackTrace();
                }
            }
            if(osw!=null){
                try {
                    osw.close();
                    osw=null;
                } catch (java.io.IOException e) {
                    e.printStackTrace();
                }
            }
            if(out!=null){
                try {
                    out.close();
                    out=null;
                } catch (java.io.IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    private void initSettings() {
        Preferences preferences = Preferences.getPreferences();
        LogFile.setText(preferences.getLogAddr());
        ExecuteNumDown.setText(String.valueOf(preferences.getExecuteNumDown()));
        ExecuteNumUp.setText(String.valueOf(preferences.getExecuteNumUp()));
        ClassPath.setText(String.valueOf(preferences.getClassFileAddr()));
        ParaAddr.setText(String.valueOf(preferences.getParaFileAddr()));
//        LogicAddr.setText(String.valueOf(preferences.getLogicFileAddr()));
        CsvAddr.setText(String.valueOf(preferences.getCsvFileAddr()));
    }
    private void initComponents() {
        mainTabPane.tabMinWidthProperty().bind(rootAnchorPane.widthProperty().divide(mainTabPane.getTabs().size()).subtract(15));
    }
    @FXML
    private void handleMenuOverdueNotification(ActionEvent event) {
        LibraryAssistantUtil.loadWindow(getClass().getResource("/library/assistant/ui/notifoverdue/overdue_notification.fxml"), "Notify Users", null);
    }
    @FXML
    private void handleSaveOptions(ActionEvent event){
        String logFile = LogFile.getText();
        String executeNumDown = ExecuteNumDown.getText();
        String executeNumUp = ExecuteNumUp.getText();
        String classPath = ClassPath.getText();
        String paraAddr = ParaAddr.getText();
//        String logicAddr = LogicAddr.getText();
        String csvAddr = CsvAddr.getText();
        Preferences preferences = new Preferences(logFile,Integer.valueOf(executeNumDown),Integer.valueOf(executeNumUp),classPath,paraAddr,csvAddr);
        Preferences.writePreferenceToFile(preferences);
    }
    @Override
    public void loadBookReturn(String bookID) {
    }
}
