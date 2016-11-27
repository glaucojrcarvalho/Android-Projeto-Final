package com.example.catenga.projetofinal;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mProdutoList;

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Produto");

        mProdutoList = (RecyclerView) findViewById(R.id.produto_list);
        mProdutoList.setHasFixedSize(true);
        mProdutoList.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {
        super.onStart();


        FirebaseRecyclerAdapter<Produto, ProdutoViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Produto, ProdutoViewHolder>(

                Produto.class,
                R.layout.produto_row,
                ProdutoViewHolder.class,
                mDatabase

        ) {
            @Override
            protected void populateViewHolder(ProdutoViewHolder viewHolder, Produto model, int position) {

                viewHolder.setNome(model.getNome());
                viewHolder.setDesc(model.getDesc());
                viewHolder.setImage(getApplicationContext(), model.getImagem());

            }
        };

        mProdutoList.setAdapter(firebaseRecyclerAdapter);


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

        return super.onOptionsItemSelected(item);
    }
}
