/** 
 * @author 吴平福 
 * E-mail:wupf@asiainfo.com 
 * @version 创建时间：2015年2月14日 上午10:11:51 
 * 类说明 
 */

package org.jpf.utils.conf;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 
 */

public final class JpfHashIni {
    private final static Map<String, Map<String, Object>> iniFile = new HashMap<String, Map<String, Object>>();
 
    private JpfHashIni() {
 
    }
 
    final public static synchronized void SetValue(String section, String key,
            Object value) {
        Map<String, Object> sectionMap = iniFile.get(section);
 
        if (sectionMap == null) {
            sectionMap = new HashMap<String, Object>();
            iniFile.put(section, sectionMap);
        }
        sectionMap.put(key, value);
    }
 
    final public static synchronized Object GetValue(String section, String key) {
        Object obj = null;
        Map<String, Object> item = iniFile.get(section);
        if (item != null) {
            obj = item.get(key);
        }
        return obj;
    }
 
    final public static synchronized void Load(String path) throws IOException {
        DataInputStream ds = new DataInputStream(new FileInputStream(path));
        String str = ds.readLine();
        String section = null;
        while (str != null) {
            // System.out.println(str);
            if (str.startsWith("[")) {
                Map<String, Object> itemMap = new HashMap<String, Object>();
                section = str.substring(1, str.length() - 1);
                // System.out.println(section);
                iniFile.put(section, itemMap);
            } else {
                Map<String, Object> itemMap = iniFile.get(section);
                String key = str.substring(0, str.indexOf("="));
                String value = str.substring(str.indexOf("=") + 1);
                itemMap.put(key, value);
            }
 
            str = ds.readLine();
        }
        ds.close();
    }
 
    final public static synchronized void WriteIni(String name) throws IOException {
        StringBuilder sb = new StringBuilder("");
        for (String section : iniFile.keySet()) {
            sb.append("[").append(section).append("]").append("\n");
            Map<String, Object> map = iniFile.get(section);
            Set<String> keySet = map.keySet();
            for (String key : keySet) {
                sb.append(key).append("=").append(map.get(key)).append("\n");
            }
 
        }
 
        File file = new File(name);
        if (!file.exists()) {
            file.createNewFile();
 
        }
        try {
 
            OutputStream os = new FileOutputStream(file);
            os.write(sb.toString().getBytes());
            os.flush();
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
 
    }
}