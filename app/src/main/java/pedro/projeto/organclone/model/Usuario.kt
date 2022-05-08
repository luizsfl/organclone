package pedro.projeto.organclone.model

import com.google.firebase.database.DatabaseReference
import pedro.projeto.organclone.config.ConfiguracaoFirebase

class Usuario {
    var nome: String
    var email: String
    var senha: String
    var idUsuario: String



    constructor(nome: String, email: String,senha : String, idUsuario :String) {
        this.nome = nome
        this.email = email
        this.senha = senha
        this.idUsuario = idUsuario
    }


        fun salvar() {
            var firebase:DatabaseReference = ConfiguracaoFirebase.getFirebaseDataBse()

            firebase.child("usuarios")
                .child(this.idUsuario)
                .child(nome)
                .setValue(this.nome)

            firebase.child("usuarios")
                .child(this.idUsuario)
                .child(email)
                .setValue(this.email)



        }



}