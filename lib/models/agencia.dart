import 'dart:convert';

/*
Cria a classe utilizada para carregar os dados que serão recebidos da API
 */
class Agencia {
    final int id;
    final String nome;

    /*
    Contrutot minímo para cada classe de modelo
     */
    const Agencia({
        this.id,
        this.nome
});

    factory Agencia.fromJson(Map<String, dynamic> json) => Agencia ( id: json['id'], nome: json['nomeAgencia']);

    Map<String, dynamic> toJson() => {
        "id": id,
        "nomeAgencia": nome,
    };
}

List<Agencia> categoriaFromJson(String str) => new List<Agencia>.from(json.decode(str).map((x) => Agencia.fromJson(x)));

String agenciaToJson(List<Agencia> data) => json.encode(new List<dynamic>.from(data.map((x) => x.toJson())));
