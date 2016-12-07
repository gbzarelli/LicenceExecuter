/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package licenceexecuter;

import java.io.IOException;
import java.text.ParseException;

/**
 *
 * @author demantoide
 */
public class ControllerKey {

    public static String encrypt(ObKeyExecuter obKey) throws IOException {
        return Encryptor.encrypt("G3$@$7htD9&jm%44", "RandomInitVector", obKey.toString());
        //return Base64.encodeBase64String(GZIPCompression.compress(en));
    }

    public static ObKeyExecuter decrypt(String key) throws ParseException, IOException {
        //key = GZIPCompression.decompress(Base64.decodeBase64(key));
        return new ObKeyExecuter().toObject(Encryptor.decrypt("G3$@$7htD9&jm%44", "RandomInitVector", key));
    }

}
