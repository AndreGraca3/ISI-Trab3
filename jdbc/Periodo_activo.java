package jdbc;

import java.time.LocalDate;

class periodo_ativo {
    private int veiculo;
    private int condutor;
    private LocalDate dtinicio;
    private LocalDate dtfim;
    private int lat;
    private int Long;
    
    periodo_ativo(String values){
    String [] atributos = values.split(";");
    veiculo = Integer.parseInt(atributos[0]);
    condutor = Integer.parseInt(atributos[1]);
    dtinicio = LocalDate.parse(atributos[2]); 
    dtfim = LocalDate.parse(atributos[3]);
    lat = Integer.parseInt(atributos[4]); 
    Long = Integer.parseInt(atributos[5]);
    }

    public Integer getVeiculo(){return veiculo;}
    public Integer getCondutor(){return condutor;}
    public LocalDate getDtInicio(){return dtinicio;}
    public LocalDate getDtFim(){return dtfim;}
    public Integer getLat(){return lat;}
    public Integer getLong(){return Long;}
    
    public void setVeiculo(int veiculo){this.veiculo = veiculo;}
    public void setCondutor(int condutor){this.condutor = condutor;}
    public void setDtInicio(LocalDate dtinicio){this.dtinicio = dtinicio;}
    public void setDtFim(LocalDate dtfim){this.dtfim = dtfim;}
    public void setLat(int lat){this.lat = lat;}
    public void setLong(int Long){this.Long = Long;}
}