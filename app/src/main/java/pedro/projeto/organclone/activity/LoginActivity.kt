package pedro.projeto.organclone.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.*
import pedro.projeto.organclone.R
import pedro.projeto.organclone.config.ConfiguracaoFirebase
import pedro.projeto.organclone.model.Usuario

class LoginActivity : AppCompatActivity() {
    private lateinit var campoEmail : EditText
    private lateinit var campoSenha : EditText

    private lateinit var botaoEntrar : Button
    private lateinit var usuario : Usuario

    private lateinit var autenticacao : FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        verificarUsuarioLogado()

        campoEmail = findViewById(R.id.editEmailLogin)
        campoSenha = findViewById(R.id.editSenhaLogin)
        botaoEntrar = findViewById(R.id.butaoEntrarLogin)

        botaoEntrar.setOnClickListener {
            val textoEmail = campoEmail.text.toString()
            val textoSenha = campoSenha.text.toString()

                if (!textoEmail.isEmpty()){
                    if (!textoSenha.isEmpty()){
                        //Caso todos os campos estejam preenchidos
                        usuario = Usuario("",textoEmail,textoSenha,"")
                        validarLogin(usuario)
                    }else{
                        Toast.makeText( this,"Preencha a senha", Toast.LENGTH_SHORT).show()
                    }
                }else{
                    Toast.makeText( this,"Preencha o E-mail", Toast.LENGTH_SHORT).show()
                }
        }

    }

    fun validarLogin(usuario: Usuario){
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao()
        autenticacao.signInWithEmailAndPassword(
            usuario.email,usuario.senha
        ).addOnCompleteListener {
            if (it.isSuccessful){
               // Toast.makeText( this,"Sucesso ao fazer login", Toast.LENGTH_SHORT).show()
                abrirTelaPrincipal()
            }
        }.addOnFailureListener {
            when {
                it is FirebaseAuthInvalidCredentialsException -> Toast.makeText( this,"Digite um E-mail e senha valido", Toast.LENGTH_SHORT).show()
                it is FirebaseAuthInvalidUserException -> Toast.makeText( this,"E-mail não esta cadastrado", Toast.LENGTH_SHORT).show()
                it is FirebaseNetworkException -> Toast.makeText( this,"Usuário sem acesso a internet", Toast.LENGTH_SHORT).show()
                else -> Toast.makeText( this,"Erro ao realizar login"+it.toString(), Toast.LENGTH_SHORT).show()
            }

        }

    }

    fun abrirTelaPrincipal(){
        startActivity(Intent(this, PrincipalActivity::class.java))
        finish()
    }

    fun verificarUsuarioLogado(){
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao()
        if (autenticacao.currentUser != null){
            abrirTelaPrincipal()
        }
    }
}