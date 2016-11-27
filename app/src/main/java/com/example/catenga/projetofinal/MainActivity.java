package com.example.catenga.projetofinal;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mProdutoList;

    private DatabaseReference mDatabase;

    private DatabaseReference mDatabaseUsers;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                if (firebaseAuth.getCurrentUser() == null){

                    Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(loginIntent);
                }
            }
        };

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Produto");
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users");

        mDatabaseUsers.keepSynced(true);

        mDatabase.keepSynced(true);


        mProdutoList = (RecyclerView) findViewById(R.id.produto_list);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);

        mProdutoList.setHasFixedSize(true);
        mProdutoList.setLayoutManager(linearLayoutManager);
    }

    @Override
    protected void onStart() {
        super.onStart();

        checkUserExist();

        mAuth.addAuthStateListener(mAuthListener);


        FirebaseRecyclerAdapter<Produto, ProdutoViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Produto, ProdutoViewHolder>(

                Produto.class,
                R.layout.produto_row,
                ProdutoViewHolder.class,
                mDatabase

        ) {
            @Override
            protected void populateViewHolder(ProdutoViewHolder viewHolder, Produto model, int position) {

                viewHolder.setNome(model.getNome());
                viewHolder.setDesc(model.getDescricao());
                viewHolder.setImage(getApplicationContext(), model.getImagem());

            }
        };

        mProdutoList.setAdapter(firebaseRecyclerAdapter);


    }

    private void checkUserExist() {

        final String user_id = mAuth.getCurrentUser().getUid();

        mDatabaseUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (!dataSnapshot.hasChild(user_id)){

                    Intent setupIntent = new Intent(MainActivity.this, SetupActivity.class);
                    setupIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(setupIntent);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public static class ProdutoViewHolder extends RecyclerView.ViewHolder{

        View mView;

        public ProdutoViewHolder(View itemView) {
            super(itemView);

            mView = itemView;
        }

        public void setNome(String nome){

            TextView produto_nome = (TextView) mView.findViewById(R.id.produto_nome);
            produto_nome.setText(nome);
        }

        public void setDesc(String desc){

            TextView produto_desc = (TextView) mView.findViewById(R.id.produto_desc);
            produto_desc.setText(desc);
        }

        public void setImage(Context ctx, String image){

            ImageView produto_image = (ImageView) mView.findViewById(R.id.produto_image);
            Picasso.with(ctx).load(image).into(produto_image);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_add){

            startActivity(new Intent(MainActivity.this, CadastrarActivity.class));
        }

        if (item.getItemId() == R.id.action_logout){
            logout();
        }

        return super.onOptionsItemSelected(item);
    }

    private void logout() {

        mAuth.signOut();
    }
}
