package pedro.projeto.organclone.config

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class ConfiguracaoFirebase {
    companion object {
        private lateinit var autenticacao : FirebaseAuth
        private lateinit var firebase : DatabaseReference
        //retorna instancia do firebaseauth
        fun getFirebaseAutenticacao():FirebaseAuth{
                autenticacao = FirebaseAuth.getInstance()

            return autenticacao
        }

        //retorna a instancia do FirebaseDataBase
        fun getFirebaseDataBse(): DatabaseReference{
            firebase = FirebaseDatabase.getInstance().getReference()
            return firebase
        }
    }

}