/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package luceneindexer;

import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Util;
import java.io.File;
import javax.swing.filechooser.FileFilter;

/**
 *
 * @author abooo
 */
public class TextFileFilter extends FileFilter{

  

    @Override
    public boolean accept(File file) {
       return file.getName().toLowerCase().endsWith("");
    }

    @Override
    public String getDescription() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

 
    
}
