package com.victor.minhaloja.ui

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.victor.minhaloja.R
import com.victor.minhaloja.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val auth by lazy { FirebaseAuth.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Já está logado? Abre direto na lista de produtos.
        if (auth.currentUser != null) {
            irParaProdutos()
            return
        }

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnEntrar.setOnClickListener { entrar() }
        binding.btnCriarConta.setOnClickListener { criarConta() }
    }

    private fun entrar() {
        val (email, senha) = lerCampos()
        if (!validar(email, senha, exigirTamanhoMinimo = false)) return

        setCarregando(true)
        auth.signInWithEmailAndPassword(email, senha)
            .addOnSuccessListener { irParaProdutos() }
            .addOnFailureListener { mostrarErro(traduzirErro(it)) }
    }

    private fun criarConta() {
        val (email, senha) = lerCampos()
        if (!validar(email, senha, exigirTamanhoMinimo = true)) return

        setCarregando(true)
        auth.createUserWithEmailAndPassword(email, senha)
            .addOnSuccessListener { irParaProdutos() } // o cadastro já deixa o usuário logado
            .addOnFailureListener { mostrarErro(traduzirErro(it)) }
    }

    private fun lerCampos(): Pair<String, String> =
        binding.etEmail.text.toString().trim() to binding.etSenha.text.toString()

    private fun validar(email: String, senha: String, exigirTamanhoMinimo: Boolean): Boolean {
        val mensagem = when {
            email.isEmpty() || senha.isEmpty() -> getString(R.string.erro_campos_vazios)
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> getString(R.string.erro_email_invalido)
            exigirTamanhoMinimo && senha.length < 6 -> getString(R.string.erro_senha_curta)
            else -> null
        }
        if (mensagem != null) {
            mostrarErro(mensagem)
            return false
        }
        binding.tvErro.visibility = View.GONE
        return true
    }

    private fun traduzirErro(e: Exception): String = when (e) {
        // WeakPassword herda de InvalidCredentials, por isso vem primeiro
        is FirebaseAuthWeakPasswordException -> getString(R.string.erro_senha_curta)
        is FirebaseAuthInvalidCredentialsException ->
            if (e.errorCode == "ERROR_INVALID_EMAIL") getString(R.string.erro_email_invalido)
            else getString(R.string.erro_credenciais)
        is FirebaseAuthInvalidUserException -> getString(R.string.erro_usuario_inexistente)
        is FirebaseAuthUserCollisionException -> getString(R.string.erro_email_em_uso)
        is FirebaseNetworkException -> getString(R.string.erro_rede)
        else -> e.localizedMessage ?: getString(R.string.erro_generico)
    }

    private fun mostrarErro(mensagem: String) {
        setCarregando(false)
        binding.tvErro.text = mensagem
        binding.tvErro.visibility = View.VISIBLE
    }

    private fun setCarregando(carregando: Boolean) {
        binding.progress.visibility = if (carregando) View.VISIBLE else View.GONE
        binding.btnEntrar.isEnabled = !carregando
        binding.btnCriarConta.isEnabled = !carregando
        if (carregando) binding.tvErro.visibility = View.GONE
    }

    private fun irParaProdutos() {
        startActivity(Intent(this, ProdutosActivity::class.java))
        finish()
    }
}
