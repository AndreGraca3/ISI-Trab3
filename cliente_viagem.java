
class cliente_viagem {
    private int idpessoa;
    private int viagem;

    cliente_viagem(String values){
        String[] attributos = values.split(","); // (2,4)
        idpessoa = Integer.parseInt(attributos[0]);
        viagem = Integer.parseInt(attributos[1]);
    }

    public Integer getidPessoa() { return idpessoa; }

    public Integer getViagem() { return viagem; }

    public void setidPessoa(int id_pessoa) { this.idpessoa = id_pessoa; }

    public void setViagem(int id_viagem) { this.viagem = id_viagem; }
}