package library.assistant.ui.settings;

import com.google.gson.Gson;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.logging.Level;
import java.util.logging.Logger;
import library.assistant.alert.AlertMaker;
import org.apache.commons.codec.digest.DigestUtils;

public class Preferences {

    public static final String CONFIG_FILE = "config.txt";

    String logAddr;
    int executeNumDown;
    int executeNumUp;
    String classFileAddr;
    String paraFileAddr;
    String csvFileAddr;

    public Preferences() {
        logAddr = "Call_Tracer_basic.csv";
        executeNumDown = 3;
        executeNumUp = 3;
        classFileAddr = "/Users/seanlxh/Downloads/web下载/未命名文件夹/csv";
        paraFileAddr = "/Users/seanlxh/Downloads/web下载/未命名文件夹/csv/paras.csv";
        csvFileAddr = "/Users/seanlxh/Downloads/web下载/未命名文件夹/csv/stock.csv";
    }

    public Preferences(String logAddr, int executeNumDown, int executeNumUp, String classFileAddr, String paraFileAddr, String csvFileAddr) {
        this.logAddr = logAddr;
        this.executeNumDown = executeNumDown;
        this.executeNumUp = executeNumUp;
        this.classFileAddr = classFileAddr;
        this.paraFileAddr = paraFileAddr;
        this.csvFileAddr = csvFileAddr;
    }

    public static String getConfigFile() {
        return CONFIG_FILE;
    }

    public String getLogAddr() {
        return logAddr;
    }

    public void setLogAddr(String logAddr) {
        this.logAddr = logAddr;
    }

    public int getExecuteNumDown() {
        return executeNumDown;
    }

    public void setExecuteNumDown(int executeNumDown) {
        this.executeNumDown = executeNumDown;
    }

    public int getExecuteNumUp() {
        return executeNumUp;
    }

    public void setExecuteNumUp(int executeNumUp) {
        this.executeNumUp = executeNumUp;
    }

    public String getClassFileAddr() {
        return classFileAddr;
    }

    public void setClassFileAddr(String classFileAddr) {
        this.classFileAddr = classFileAddr;
    }

    public String getParaFileAddr() {
        return paraFileAddr;
    }

    public void setParaFileAddr(String paraFileAddr) {
        this.paraFileAddr = paraFileAddr;
    }

    public String getCsvFileAddr() {
        return csvFileAddr;
    }

    public void setCsvFileAddr(String csvFileAddr) {
        this.csvFileAddr = csvFileAddr;
    }

    public static void initConfig() {
        Writer writer = null;
        try {
            Preferences preference = new Preferences();
            Gson gson = new Gson();
            writer = new FileWriter(CONFIG_FILE);
            gson.toJson(preference, writer);
        } catch (IOException ex) {
            Logger.getLogger(Preferences.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                writer.close();
            } catch (IOException ex) {
                Logger.getLogger(Preferences.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static Preferences getPreferences() {
        Gson gson = new Gson();
        Preferences preferences = new Preferences();
        try {
            preferences = gson.fromJson(new FileReader(CONFIG_FILE), Preferences.class);
        } catch (FileNotFoundException ex) {
            initConfig();
            Logger.getLogger(Preferences.class.getName()).log(Level.SEVERE, null, ex);
        }
        return preferences;
    }

    public static void writePreferenceToFile(Preferences preference) {
        Writer writer = null;
        try {
            Gson gson = new Gson();
            writer = new FileWriter(CONFIG_FILE);
            gson.toJson(preference, writer);

            AlertMaker.showSimpleAlert("成功", "设置更新成功");
        } catch (IOException ex) {
            Logger.getLogger(Preferences.class.getName()).log(Level.SEVERE, null, ex);
            AlertMaker.showErrorMessage(ex, "失败", "无法保存配置文件");
        } finally {
            try {
                writer.close();
            } catch (IOException ex) {
                Logger.getLogger(Preferences.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
