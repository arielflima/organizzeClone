package com.example.organizze.Activity;

import android.graphics.Bitmap;
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
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DespesasActivity extends AppCompatActivity {

    private TextInputEditText campoData, campoCategoria, campoDescricao;
    private EditText campoValor;
    private Movimentacao movimentacao;
    private FirebaseAuth  autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
    private DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
    private Double despesasTotal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_despesas);

        campoCategoria = findViewById(R.id.editCategoria);
        campoData = findViewById(R.id.editData);
        campoValor = findViewById(R.id.editValor);
        campoDescricao = findViewById(R.id.editDescricao);

        campoData.setText(DateCustom.dataAtual());
        recurarDespesasTotal();
    }

    public void salvarDespesas(View view) {

        if (validarDespesas()) {

            Double valorRecuperado = Double.parseDouble(campoValor.getText().toString());
            String data = campoData.getText().toString();
            movimentacao = new Movimentacao();
            movimentacao.setValor(valorRecuperado);
            movimentacao.setCategoria(campoCategoria.getText().toString());
            movimentacao.setDescricao(campoDescricao.getText().toString());
            // movimentacao.setData(campoData.getText().toString());
            movimentacao.setTipo("d");


            Double despesaAtualizada = despesasTotal + valorRecuperado;
            atualizarDespesas(despesaAtualizada);

            movimentacao.salvar(data);
            finish();
        }
    }

    public Boolean validarDespesas() {
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
                        Toast.makeText(DespesasActivity.this, "Data não foi preenchido!", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                } else {
                    Toast.makeText(DespesasActivity.this, "Categoria não foi preenchido!", Toast.LENGTH_SHORT).show();
                    return false;
                }
            } else {
                Toast.makeText(DespesasActivity.this, "Descrição não foi preenchido!", Toast.LENGTH_SHORT).show();
                return false;
            }
        } else {
            Toast.makeText(DespesasActivity.this, "Valor não foi preenchido!", Toast.LENGTH_SHORT).show();
            return false;
        }

    }

    public void recurarDespesasTotal(){
        String emailUsuario = autenticacao.getCurrentUser().getEmail();
        String idUsuario = Base64Custom.codificarBase64(emailUsuario);
        DatabaseReference usuarioRef = firebaseRef.child("usuarios").child(idUsuario);

        usuarioRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Usuario usuario = dataSnapshot.getValue(Usuario.class);
                despesasTotal = usuario.getDespesasTotal();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void atualizarDespesas(Double despesa){
        String emailUsuario = autenticacao.getCurrentUser().getEmail();
        String idUsuario = Base64Custom.codificarBase64(emailUsuario);
        DatabaseReference usuarioRef = firebaseRef.child("usuarios").child(idUsuario);

        usuarioRef.child("despesasTotal").setValue(despesa);


    }
}
