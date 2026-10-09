package model;

public class Artista {
    private int id;
    private String nome;
    private String tipo; // Cantor ou Banda
    private String pais;

    public Artista() {}
    public Artista(int id, String nome, String tipo, String pais) {
        this.id = id; this.nome = nome; this.tipo = tipo; this.pais = pais;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public String getPais() { return pais; }
    public void setPais(String pais) { this.pais = pais; }

    @Override
    public String toString() { return nome; } // usado nos JComboBox
}
