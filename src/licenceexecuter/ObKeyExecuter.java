/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package licenceexecuter;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

/**
 *
 * @author demantoide
 */
public class ObKeyExecuter implements Serializable {

    private Date dataLicenca;
    private Date dataValidade;
    private Date ultimaDataVerificada;
    private String executer;

    public ObKeyExecuter() {
    }

    public Date getDataLicenca() {
        return dataLicenca;
    }

    public void setDataLicenca(Date dataLicenca) {
        this.dataLicenca = dataLicenca;
    }

    public Date getDataValidade() {
        return dataValidade;
    }

    public void setDataValidade(Date dataValidade) {
        this.dataValidade = dataValidade;
    }

    public Date getUltimaDataVerificada() {
        return ultimaDataVerificada;
    }

    public void setUltimaDataVerificada(Date ultimaDataVerificada) {
        this.ultimaDataVerificada = ultimaDataVerificada;
    }

    public String getExecuter() {
        return executer;
    }

    public void setExecuter(String executer) {
        this.executer = executer;
    }

    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String stDataLicenca = sdf.format(dataLicenca);
        String stDataValidade = sdf.format(dataValidade);
        String stDataUltimaVerificada = sdf.format(ultimaDataVerificada);
        return stDataLicenca + "|" + stDataValidade + "|" + stDataUltimaVerificada + "|" + executer;
    }

    public ObKeyExecuter toObject(String object) throws ParseException {
        String[] split = object.split(Pattern.quote("|"));

        String stDataLicenca = split[0];
        String stDataValidade = split[1];
        String stDataUltimaVerificada = split[2];
        executer = split[3];

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        dataLicenca = sdf.parse(stDataLicenca);
        dataValidade = sdf.parse(stDataValidade);
        ultimaDataVerificada = sdf.parse(stDataUltimaVerificada);
        return this;
    }

}
