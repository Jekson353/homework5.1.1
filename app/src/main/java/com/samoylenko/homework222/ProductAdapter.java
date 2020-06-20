package com.samoylenko.homework222;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends BaseAdapter {

    private List<Product> products;
    private LayoutInflater inflater;
    private Context ctx;

    ProductAdapter(Context context, List<Product> products) {
        if (products == null) {
            this.products = new ArrayList<>();
        } else {
            this.products = products;
        }
        this.ctx = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    void addItem(Product products) {
        this.products.add(products);
        notifyDataSetChanged();
    }

    private void removeItem(int position) {
        products.remove(position);
        notifyDataSetChanged();
    }

    // кол-во элементов
    @Override
    public int getCount() {
        return products.size();
    }

    // элемент по позиции
    @Override
    public Product getItem(int position) {
        if (position < products.size()) {
            return products.get(position);
        } else {
            return null;
        }
    }

    // id по позиции
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = inflater.inflate(R.layout.content_main, parent, false);
        }

        Product prod = products.get(position);

        ImageView image = view.findViewById(R.id.icon_prod);
        TextView tittle = view.findViewById(R.id.title_prod);
        TextView subtitle = view.findViewById(R.id.subtittle_prod);
        Button btn = view.findViewById(R.id.checkBox_prod);

        tittle.setText(prod.getTittle());
        subtitle.setText(prod.getSubtitle());
        image.setImageDrawable(prod.getImage());
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeItem((Integer) v.getTag());
                removeFromFile();
            }
        });
        btn.setTag(position);
        return view;
    }

    //удаление записи из файла
    private void removeFromFile() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < products.size(); i++) {
            sb.append(products.get(i).getTittle()).append(";");
        }
        String value = sb.toString();

        //создаем временный файл, чтобы не потерять данные
        //на случай, если во время операции произойдет непредвиденная ошибка (выключился телефон, ушел в ребут и т.д.)
        File newFile = new File(this.ctx.getExternalFilesDir(null), CustomActivity.tmpFileName);
        File oldFile = new File(this.ctx.getExternalFilesDir(null), CustomActivity.fileName);

        String lineSeparator = System.getProperty("line.separator");

        try (FileWriter writer = new FileWriter(newFile, true)) {
            writer.append(lineSeparator).append(value);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //временный файл записан, значит можно его подменить
        if (oldFile.delete()) {
            newFile.renameTo(oldFile);
        }
    }
}
