package com.example.organizze.Activity;

import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.organizze.R;
import com.example.organizze.config.ConfiguracaoFirebase;
import com.example.organizze.helper.Base64Custom;
import com.example.organizze.helper.DateCustom;
import com.example.organizze.model.Movimentacao;
import com.example.organizze.model.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class ReceitaActivity extends AppCompatActivity {

    private TextInputEditText campoData, campoCategoria, campoDescricao;
    private EditText campoValor;
    private Movimentacao movimentacao;
    private FirebaseAuth autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
    private DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
    private Double receitaTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receita);

        campoCategoria = findViewById(R.id.editCategoria);
        campoData = findViewById(R.id.editData);
        campoValor = findViewById(R.id.editValor);
        campoDescricao = findViewById(R.id.editDescricao);

        campoData.setText(DateCustom.dataAtual());
        recurarReceitaTotal();
    }

    public void salvarReceita(View view) {

        if (validarReceita()) {

            Double valorRecuperado = Double.parseDouble(campoValor.getText().toString());
            String data = campoData.getText().toString();
            movimentacao = new Movimentacao();
            movimentacao.setValor(valorRecuperado);
            movimentacao.setCategoria(campoCategoria.getText().toString());
            movimentacao.setDescricao(campoDescricao.getText().toString());
            // movimentacao.setData(campoData.getText().toString());
            movimentacao.setTipo("r");


            Double receitaAtualizada = receitaTotal + valorRecuperado;
            atualizarReceita(receitaAtualizada);

            movimentacao.salvar(data);
            finish();
        }
    }
    public Boolean validarReceita() {
        String textoValor = campoValor.getText().toString();
        String textoCategoria = campoCategoria.getText().toString();
        String textoDescricao = campoDescricao.getText().toString();
        String textoData = campoData.getText().toString();

        if (!textoValor.isEmpty()) {
            if (!textoDescricao.isEmpty()) {
                if (!textoCategoria.isEmpty()) {
                    if (!textoData.isEmpty()) {
                        return true;
                    } else {
                        Toast.makeText(ReceitaActivity.this, "Data não foi preenchido!", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                } else {
                    Toast.makeText(ReceitaActivity.this, "Categoria não foi preenchido!", Toast.LENGTH_SHORT).show();
                    return false;
                }
            } else {
                Toast.makeText(ReceitaActivity.this, "Descrição não foi preenchido!", Toast.LENGTH_SHORT).show();
                return false;
            }
        } else {
            Toast.makeText(ReceitaActivity.this, "Valor não foi preenchido!", Toast.LENGTH_SHORT).show();
            return false;
        }

    }

    public void recurarReceitaTotal(){
        String emailUsuario = autenticacao.getCurrentUser().getEmail();
        String idUsuario = Base64Custom.codificarBase64(emailUsuario);
        DatabaseReference usuarioRef = firebaseRef.child("usuarios").child(idUsuario);

        usuarioRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Usuario usuario = dataSnapshot.getValue(Usuario.class);
                receitaTotal = usuario.getReceitaTotal();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void atualizarReceita(Double receita){
        String emailUsuario = autenticacao.getCurrentUser().getEmail();
        String idUsuario = Base64Custom.codificarBase64(emailUsuario);
        DatabaseReference usuarioRef = firebaseRef.child("usuarios").child(idUsuario);

        usuarioRef.child("receitaTotal").setValue(receita);


    }
}
