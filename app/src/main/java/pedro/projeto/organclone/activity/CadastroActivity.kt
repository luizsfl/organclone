package pedro.projeto.organclone.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import pedro.projeto.organclone.R
import pedro.projeto.organclone.config.ConfiguracaoFirebase
import pedro.projeto.organclone.helper.Base64Custom
import pedro.projeto.organclone.model.Usuario

class CadastroActivity : AppCompatActivity() {

    private lateinit var camponome: EditText
    private lateinit var campoEmail: EditText
    private lateinit var campoSenha: EditText

    private lateinit var botaoCadastrar: Button
    private lateinit var autenticacao: FirebaseAuth

    private lateinit var usuario : Usuario

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro)

        supportActionBar?.title = "Cadastro"

        camponome  = findViewById(R.id.editNome)
        campoEmail = findViewById(R.id.editEmail)
        campoSenha = findViewById(R.id.editSenha)
        botaoCadastrar = findViewById(R.id.butonCadastrar)

        botaoCadastrar.setOnClickListener {
            val textoNome = camponome.text.toString()
            val textoEmail = campoEmail.text.toString()
            val textoSenha = campoSenha.text.toString()
            
            if (!textoNome.isEmpty()){
                if (!textoEmail.isEmpty()){
                    if (!textoSenha.isEmpty()){
                        //Caso todos os campos estejam preenchidos
                            usuario = Usuario(textoNome,textoEmail,textoSenha,"")
                            cadastrarUsuario(usuario)
                    }else{
                        Toast.makeText( this,"Preencha a senha", Toast.LENGTH_SHORT).show()
                    }
                }else{
                    Toast.makeText( this,"Preencha o E-mail", Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText( this,"Preencha o nome", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun cadastrarUsuario(usuario:Usuario){
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao()
        autenticacao.createUserWithEmailAndPassword(
            usuario.email,usuario.senha
        ).addOnCompleteListener{
            if (it.isSuccessful) {
                //cadastrado
                var  idUsuario = Base64Custom.codificarBase64(usuario.email)
                usuario.idUsuario = idUsuario
                usuario.salvar()
                finish()
            }
        }.addOnFailureListener {
            when {
                it is FirebaseAuthWeakPasswordException -> Toast.makeText( this,"Digite uma senha com no minimo 6 caracteres", Toast.LENGTH_SHORT).show()
                it is FirebaseAuthUserCollisionException -> Toast.makeText( this,"E-mail já cadastrado", Toast.LENGTH_SHORT).show()
                it is FirebaseNetworkException -> Toast.makeText( this,"Usuário sem acesso a internet", Toast.LENGTH_SHORT).show()
                else -> Toast.makeText( this,"Erro ao cadastrar"+it.toString(), Toast.LENGTH_SHORT).show()
            }

        }
    }
}