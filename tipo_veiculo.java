
class tipo_veiculo {
    private int tipo;
    private int nlugares;
    private int multiplicador;
    private String designacao;

    tipo_veiculo(String values){
        String[] attributos = values.split(","); // ()
        tipo = Integer.parseInt(attributos[0]);
        nlugares = Integer.parseInt(attributos[1]);
        multiplicador = Integer.parseInt(attributos[2]);
        designacao = attributos[3];
    }

    public Integer getTipo() { return tipo; }

    public Integer getNLugares() { return nlugares; }

    public Integer getMultiplicador() { return multiplicador; }

    public String getDesignacao() { return designacao; }

    public void setTipo(int tipo) { this.tipo = tipo; }

    public void setNLugares(int nlugares) { this.nlugares = nlugares; }

    public void setMultiplicador(int multiplicador) { this.multiplicador = multiplicador; }

    public void setDesignacao(String designacao) { this.designacao = designacao; }
}