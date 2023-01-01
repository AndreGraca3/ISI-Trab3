package jdbc;

class Contacto {
    private int idpessoa;
    private String ntelefone;
    
    contacto (String values){
        String[] attributos = values.split(","); // ()
        idpessoa = Integer.parseInt(attributos[0]);
        ntelefone = attributos[1];
    }

    public Integer getIdPessoa(){ return idpessoa;}
    public String getNTelefone(){return ntelefone;} 

    public void setIdPessoa(int idPessoa){this.idpessoa = idPessoa;}
    public void setNTelefone(String ntelefone){this.ntelefone =  ntelefone;}
    
}