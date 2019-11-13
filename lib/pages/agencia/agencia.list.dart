
import 'package:flutter/material.dart';
import 'dart:async';
import 'package:http/http.dart' as http;
import 'dart:convert';
import 'package:freejob/models/agencia.dart';
import 'package:freejob/services/entity_services/agencia.service.dart';
import 'agencia.detail.dart';

class AgenciaListPage extends StatefulWidget {
  @override
  _AgenciaListPageState createState() => _AgenciaListPageState();
}

final String _title = "Lista Agências";

class _AgenciaListPageState extends State<AgenciaListPage> {

    Future<List<Agencia>> _getAgencias() async{

        var data = await http.get(API_AGENCIA  + API_AGENCIA_TEST);

        var jsonData = json.decode(data.body);

        List<Agencia> agencias = [];

        for (var u in jsonData) {
            Agencia agencia = Agencia(id: u["id"], nome: u["agenciaNome"]);
            agencias.add(agencia);

        }

        print("Quantidade de agencias ====>>>> " + agencias.length.toString());

        return agencias;
    } // [final do Future]

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(_title),
        elevation: 5.0, // Removing the drop shadow cast by the app bar.
      ),
      body: FutureBuilder(
          future: agencias(),
          builder: (BuildContext context, AsyncSnapshot snapshot) {
            print(snapshot.data);
            if (snapshot.data == null) {
              return Container(child: Center(child: Text("Loading...")));
            } else {
              if (snapshot.connectionState == ConnectionState.done) {
                  print("Conexão concluída!!!");
                return AgenciaList(items: snapshot.data);
              } else {
                return Center(child: CircularProgressIndicator());
              }
            }
          } // builder
          ),
    );
  }
}

Widget item(int id, String nome, BuildContext context, Agencia agencia) {
  return ListTile(
      title: Text(nome,
          style: TextStyle(fontWeight: FontWeight.w500, fontSize: 20.0)),
      onTap: () {
        Navigator.push(context,
            MaterialPageRoute(builder: (context) => AgenciaDetail(
                data: agencia)
            )
          );
      },
      leading: Icon(Icons.person, size: 50, color: Colors.blue[500]));
}

class AgenciaList extends StatelessWidget {
  final List<Agencia> items;
  AgenciaList({this.items});

  @override
  Widget build(BuildContext context) {
    //final parsed = json.decode(data).cast<Map<String, dynamic>>();
    List<Agencia> _items = items;
        //parsed.map<Agencia>((json) => Agencia.fromJson(json)).toList();
    print("Total de Agencias: ===>>> " + _items.length.toString());
    return ListView.builder(
      itemCount: _items.length,
      itemBuilder: (context, index) {
        final agencia = _items[index];
        return item(agencia.id, agencia.nome, context, agencia);
      },
    );
  }
}
