package com.samoylenko.homework222;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

public class CustomActivity extends AppCompatActivity {

    public ProductAdapter prodAdapter;
    //картинки
    private List<Drawable> images = new ArrayList<>();
    public static final String fileName = "log.txt";
    public static final String tmpFileName = "temp";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_list_view);

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        myToolbar.setTitle(R.string.tittle_main);
        setSupportActionBar(myToolbar);

        init();
    }

    public void init() {
        img();

        FloatingActionButton fab = findViewById(R.id.fab);
        ListView listView = findViewById(R.id.listview_p);

        Bundle arguments = getIntent().getExtras();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addData(newString());
            }
        });

        prodAdapter = new ProductAdapter(this, null);
        listView.setAdapter(prodAdapter);

        getDataFromFile();

        //если есть переданные данные, добавляем
        if (arguments != null) {
            String name = arguments.get("newStr").toString();
            addData(name);
        }
    }

    //первоначальная инициализация
    //заполнение из файла strings.xml в файл log.txt либо сразу из файла
    public void getDataFromFile() {
        String[] arrayContent;
        String newString = getString(R.string.large_text).replace("\n\n", ";");

        if (stringToFile(newString)) {
            //Если все хорошо, можем пробовать читать
            arrayContent = getFromFile().split(";");

            Map<String, String> m;

            for (String s : arrayContent) {
                prodAdapter.addItem(new Product(
                        s + "",
                        s.length() + "",
                        images.get(new Random().nextInt(images.size()))
                ));
            }
        } else {
            prodAdapter.addItem(new Product(
                    "Hello",
                    "Sub",
                    images.get(new Random().nextInt(images.size()))
            ));
        }
    }

    //Добавление новых строк
    public void addData(String value) {
        prodAdapter.addItem(new Product(
                value + "",
                value.length() + "",
                images.get(new Random().nextInt(images.size()))
        ));
        addToFile(value);
    }

    private void img() {
        images.add(getDrawable(R.drawable.ic_add_alarm_black_24dp));
        images.add(getDrawable(R.drawable.ic_add_circle_black_24dp));
        images.add(getDrawable(R.drawable.ic_card_membership_black_24dp));
        images.add(getDrawable(R.drawable.ic_cloud_circle_black_24dp));
        images.add(getDrawable(R.drawable.ic_content_cut_black_24dp));
        images.add(getDrawable(R.drawable.ic_desktop_mac_black_24dp));
        images.add(getDrawable(R.drawable.ic_group_work_black_24dp));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_main) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            Toast.makeText(this, R.string.toast_main, Toast.LENGTH_LONG).show();
            return true;
        }
        if (id == R.id.action_custom_adapter) {
            Intent intent = new Intent(getApplicationContext(), CustomActivity.class);
            startActivity(intent);
            Toast.makeText(this, R.string.toast_adapter, Toast.LENGTH_LONG).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //Добавление данных в файл
    public void addToFile(String value) {

        File logFile = new File(getApplicationContext().getExternalFilesDir(null), fileName);
        String lineSeparator = System.getProperty("line.separator");
        FileWriter writer = null;
        try {
            writer = new FileWriter(logFile, true);
            writer.append(lineSeparator + value + ";");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //Предполагается, что файла log.txt изначально не существует, поэтому
    //производится начальное добавление в файл текста, который находится в String-ах
    public boolean stringToFile(String string) {
        //Проверяем, можем ли мы писать
        if (isExternalStorageWritable()) {
            File logFile = new File(getApplicationContext().getExternalFilesDir(null), fileName);
            //если файл уже существует, новый файл не создаем, сразу вернем true, т.к. значит из него можно попробовать извлекать данные
            if (logFile.exists() && logFile.isFile()) {
                return true;
            } else {
                FileWriter writer = null;
                try {
                    writer = new FileWriter(logFile, true);
                    writer.append(string);
                    writer.close();
                    return true;
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                } finally {
                    try {
                        writer.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            return false;
        }
    }

    //функция чтения данных из файла
    public String getFromFile() {
        FileReader fileReader = null;
        String content = "";
        //проверяем, можем ли мы читать
        if (isExternalStorageReadable()) {
            File logFile = new File(getApplicationContext().getExternalFilesDir(null), fileName);

            //проверка существования файла
            if (logFile.exists() && logFile.isFile()) {
                //если существует, то читаем его
                try {
                    fileReader = new FileReader(logFile);
                    Scanner scan = new Scanner(fileReader);
                    int i = 1;
                    while (scan.hasNextLine()) {
                        content += scan.nextLine();
                        i++;
                    }
                    fileReader.close();
                    return content;
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        fileReader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                Toast.makeText(CustomActivity.this, "Файла не существует!"
                        , Toast.LENGTH_LONG)
                        .show();
            }
        } else {
            Toast.makeText(CustomActivity.this, "Чтение не удалось. Возможно он был неожиданно удален..."
                    , Toast.LENGTH_LONG)
                    .show();
        }
        return content;
    }

    //генерация новых слов
    public String newString() {
        String CHAR_LOWER = "abcdefghijklmnopqrstuvwxyz";
        int random = (int) (Math.random() * 15) + 1;
        if (random < 1) throw new IllegalArgumentException();

        StringBuilder sb = new StringBuilder(random);
        for (int i = 0; i < random; i++) {
            char rndChar = CHAR_LOWER.charAt(i);
            sb.append(rndChar);
        }
        return sb.toString();
    }

    /* Проверка внутреннего хранилища на доступность записи */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Проверка внутреннего хранилища на доступность чтения */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }
}
