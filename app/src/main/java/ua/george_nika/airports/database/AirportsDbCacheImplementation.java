package ua.george_nika.airports.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import ua.george_nika.airports.R;
import ua.george_nika.airports.data.GlobalContextAndData;

public class AirportsDbCacheImplementation extends AirportsDbAbstract{

    private static final String DB_NAME = "airports.db";
    private static final File dbFile;
    private static Context DbContext;

    static {
        DbContext = GlobalContextAndData.getContext();
        //получаем путь к SD-карте.
        File DB_PATH = DbContext.getExternalCacheDir();
        DB_PATH.mkdirs();
        //проверяем есть ли уже файл БД на карте
        dbFile = new File(DB_PATH, DB_NAME);
    }

    @Override
    public void preparedDbForWork() {
        createDataBase();
        closeDbAfterWork();
    }

    private void createDataBase()  {
        if (!dbFile.exists()) {
            //если файла нет, то попытаемся его создать
            try {
                dbFile.createNewFile();
                copyFromZipFile();
            } catch (IOException e) {
                throw new Error("Error creating database", e);
            }
        }
    }


    private void copyFromZipFile() throws IOException {
        InputStream is = DbContext.getResources().openRawResource(R.raw.airports);
        File outFile = dbFile;
        OutputStream myOutput = new FileOutputStream(outFile.getAbsolutePath());
        ZipInputStream zis = new ZipInputStream(new BufferedInputStream(is));
        try {
            ZipEntry ze;
            while ((ze = zis.getNextEntry()) != null) {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int count;

                while ((count = zis.read(buffer)) != -1) {
                    byteArrayOutputStream.write(buffer, 0, count);
                }
                byteArrayOutputStream.writeTo(myOutput);
            }
        } finally {
            zis.close();
            myOutput.flush();
            myOutput.close();
            is.close();
        }
    }

    @Override
    protected SQLiteDatabase getVariableDbForWork() {
        return SQLiteDatabase.openDatabase(dbFile.getAbsolutePath(), null, SQLiteDatabase.OPEN_READWRITE);
    }

    @Override
    public void closeVariableDbAfterWork(SQLiteDatabase dbForWork) {
        if (dbForWork !=null){
            dbForWork.close();
        }
    }

    @Override
    public void closeDbAfterWork() {
    }
}
