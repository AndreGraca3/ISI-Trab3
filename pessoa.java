class pessoa{
    private int id;
    private String noident;
    private String nif;
    private String nproprio;
    private String apelido;
    private String morada;
    private int codpostal;
    private String localidade;
    private String atrdisc;

    pessoa(String values){
        String[] attributos = values.split(","); // (1,23,54,Diogo,Santos,Rua roxa,2635,Sintra,C)
        id = Integer.parseInt(attributos[0]);
        noident = attributos[1];
        nif = attributos[2];
        nproprio = attributos[3];
        apelido = attributos[4];
        morada = attributos[5];
        codpostal = Integer.parseInt(attributos[6]);
        localidade = attributos[7];
        atrdisc = attributos[8];
    }

    public Integer getId() { return id; }

    public String getNoident() { return noident; }

    public String getNif() { return nif; }

    public String getNproprio() { return nproprio; }

    public String getApelido() { return apelido; }

    public String getMorada() { return morada; }

    public Integer getCodpostal() { return codpostal; }

    public String getLocalidade() { return localidade; }

    public String getAtrdisc() { return atrdisc; }

    public void setId(int id_pessoa) { this.id = id_pessoa; }

    public void setNoident(String noident) { this.noident = noident; }

    public void setNif(String nif) { this.nif = nif; }

    public void setNproprio(String nproprio) { this.nproprio = nproprio; }

    public void setApelido(String apelido) { this.apelido = apelido; }

    public void setMorada(String morada) { this.morada = morada; }

    public void setCodpostal(int codpostal) { this.codpostal = codpostal; }

    public void setLocalidade(String localidade) { this.localidade = localidade; }

    public void setAtrdisc(String atrdisc) { this.atrdisc = atrdisc; }
}