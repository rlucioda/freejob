import 'package:flutter/material.dart';
import '../../models/agencia.dart';
import 'agencia.form.dart';
import '../../services/entity_services/agencia.service.dart';

class AgenciaDetail extends StatelessWidget {
    int id;
    final Agencia data;
    AgenciaDetail({@required this.data});


    @override
  Widget build(BuildContext context) {
    return Scaffold(
        appBar: AppBar(
            title: Text("Detalhe Agência"),
            elevation: 5.0, // Removing the drop shadow cast by the app bar.
        ),
        body: Center (
            child: Text("Agência: ${data.nome}"),
        ),
    );
  }
}
