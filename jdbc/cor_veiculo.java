package jdbc;

class Cor_veiculo {
    private int veiculo;
    private String cor;

    Cor_veiculo(String values){
        String[] attributos = values.split(","); // (2,"vermelho")
        veiculo = Integer.parseInt(attributos[0]); // converter o valor a inteiro.
        cor = attributos[1];
    }

    public Integer getVeiculo() { return veiculo; }

    public String getCor() { return cor; }

    public void setVeiculo(int id_veiculo) { this.veiculo = id_veiculo; }

    public void setCor(String cor) { this.cor = cor; }
}