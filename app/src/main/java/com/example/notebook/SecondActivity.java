package com.example.notebook;

import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class SecondActivity extends AppCompatActivity {

    // создание полей
    private RecyclerView recyclerView;
    private FloatingActionButton fabAdd, deleteAdd;

    private List<Notebook> notesList;

    private DatabaseHelper database;
    private Adapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        // присваивание id полям
        recyclerView = findViewById(R.id.recycler_list);
        fabAdd = findViewById(R.id.fabAdd);
        deleteAdd = findViewById(R.id.deleteAdd);

        notesList = new ArrayList<>(); // выделение памяти и задание типа контейнера для списка заметок
        database = new DatabaseHelper(this); // выделение памяти и задание текущего контекста работы с БД

        // считывание данных из БД и запись их в коллекцию notesList
        fetchAllNotes();

        recyclerView.setLayoutManager(new LinearLayoutManager(this)); // задание структуры вывода данных в recyclerView
        adapter = new Adapter(this, SecondActivity.this, notesList); // инициализация адаптера и передача в рего данных из БД
        recyclerView.setAdapter(adapter); // передача в recyclerView адаптер

        // обработка нажатия кнопки создания новой заметки
        deleteAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                database.deleteAllNotes(DatabaseHelper.columnId);
                startActivity(new Intent(SecondActivity.this, SecondActivity.class));
            }
        });
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // переключение на новую активность
                startActivity(new Intent(SecondActivity.this, AddNotesActivity.class));
            }
        });
    }

    // метод считывания из БД всех записей
    public void fetchAllNotes(){
        // чтение БД и запись данных в курсор
        Cursor cursor = database.readNotes();

        if (cursor.getCount() == 0) { // если данные отсутствую, то вывод на экран об этом тоста
            Toast.makeText(this, "Заметок нет", Toast.LENGTH_SHORT).show();
        } else { // иначе помещение их в контейнер данных notesList
            while (cursor.moveToNext()){
                // помещение в контейнер notesList из курсора данных
                notesList.add(new Notebook(cursor.getString(0), cursor.getString(1), cursor.getString(2)));
            }
        }
    }
}