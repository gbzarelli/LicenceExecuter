/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package licenceexecuter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import javax.swing.JOptionPane;
import org.apache.commons.codec.binary.Base64;

/**
 *
 * @author demantoide
 */
public class LicenceExecuter {

    /**
     * @param args the command line arguments
     * @throws java.text.ParseException
     */
    public static void main(String[] args) throws Throwable {
        LicenceExecuter licenceExecuter = new LicenceExecuter();
        licenceExecuter.initProperties();

        if (args != null && args.length >= 5) {
            System.out.println(Base64.encodeBase64String(args[4].getBytes()));
            System.out.println(licenceExecuter.properties.getProperty("generateKey"));
            if (Base64.encodeBase64String(args[4].getBytes()).equals(licenceExecuter.properties.getProperty("generateKey"))) {
                System.out.println(ControllerKey.encrypt(new ObKeyExecuter().toObject(args[0] + "|" + args[1] + "|" + args[2] + "|" + args[3])));
            } else {
                throw new IllegalArgumentException("InvalidPassword");
            }
            return;
        }
        try {
            licenceExecuter.run();
        } catch (Throwable ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "PROBLEMAS NA EXECUÇÃO", "ATENÇÃO", JOptionPane.ERROR_MESSAGE);
        }
    }

    private Properties properties;
    private File fileProperties;

    private void initProperties() throws Throwable {
        fileProperties = new File("./central.properties");
        properties = new Properties();
        properties.load(new FileInputStream(fileProperties));
    }

    private void run() throws Throwable {
        Date atualDate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        ObKeyExecuter obKey = ControllerKey.decrypt(properties.getProperty("key"));
        if (atualDate.before(obKey.getDataLicenca())) {
            JOptionPane.showMessageDialog(null, "SUA LICENÇA INICIA DIA: " + sdf.format(obKey.getDataLicenca()), "ATENÇÃO", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (atualDate.after(obKey.getUltimaDataVerificada())) {
            try {
                if (atualDate.before(obKey.getDataValidade())) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(obKey.getDataValidade());
                    calendar.add(Calendar.DAY_OF_MONTH, -6);
                    if (atualDate.after(calendar.getTime())) {
                        String msg = String.format("SUA LICENÇA VENCE NO DIA %s ENTRE COM A NOVA CHAVE OU CONTINUE.", sdf.format(obKey.getDataValidade()));
                        JDialogRegister jDialogRegister = new JDialogRegister(msg);
                        jDialogRegister.setVisible(true);
                        if (jDialogRegister.getNewKey() != null && !jDialogRegister.getNewKey().isEmpty()) {
                            ObKeyExecuter ob2 = register(jDialogRegister.getNewKey());
                            obKey = ob2 == null ? obKey : ob2;
                        }
                    }
                } else {
                    String msg = String.format("SUA LICENÇA VENCEU NO DIA %s ENTRE COM A NOVA CHAVE OU CONTINUE PARA SAIR.", sdf.format(obKey.getDataValidade()));
                    JDialogRegister jDialogRegister = new JDialogRegister(msg);
                    jDialogRegister.setVisible(true);
                    if (jDialogRegister.getNewKey() != null && !jDialogRegister.getNewKey().isEmpty()) {
                        ObKeyExecuter ob2 = register(jDialogRegister.getNewKey());
                        if (ob2 == null) {
                            return;
                        }
                        obKey = ob2;
                    } else {
                        return;
                    }
                }
                executeProgram(obKey);
            } finally {
                obKey.setUltimaDataVerificada(atualDate);
                updateKey(obKey);
            }
        } else {
            JOptionPane.showMessageDialog(null, "POSSÍVEL ALTERAÇÃO NO RELÓGIO DO SISTEMA! VERIFIQUE", "ATENÇÃO", JOptionPane.ERROR_MESSAGE);
        }

    }

    private ObKeyExecuter register(String newKey) {
        try {
            ObKeyExecuter ob = ControllerKey.decrypt(newKey);
            if (new Date().before(ob.getDataValidade())) {
                updateKey(ob);
                JOptionPane.showMessageDialog(null, "NOVA CHAVE ATIVADA COM SUCESSO");
                return ob;
            } else {
                JOptionPane.showMessageDialog(null, "NOVA CHAVE ESTA VENCIDA, IMPOSSIVEL CONTINUAR.");
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
        JOptionPane.showMessageDialog(null, "CHAVE INVÁLIDA", "ATENÇÃO", JOptionPane.ERROR_MESSAGE);
        return null;
    }

    private void executeProgram(ObKeyExecuter obKey) throws IOException {
        Process exec = Runtime.getRuntime().exec(obKey.getExecuter());
    }

    private void updateKey(ObKeyExecuter obKey) throws Throwable {
        properties.setProperty("key", ControllerKey.encrypt(obKey));
        properties.store(new FileOutputStream(fileProperties), "UpdateKey");
    }

}
