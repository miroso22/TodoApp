package com.example.todo

import android.content.Context
import androidx.room.Room
import com.example.todo.data.db.AppDatabase
import com.example.todo.data.repository.TaskRepository
import com.example.todo.ui.screen.MainViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.context.GlobalContext
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

private const val DB_NAME = "app_database"
private val dbModule = module {
    single {
        Room.databaseBuilder(
            androidContext().applicationContext, AppDatabase::class.java, DB_NAME
        ).build()
    }

    single { get<AppDatabase>().taskDao() }
}

val mainModule = module {
    includes(dbModule)

    singleOf(::TaskRepository)
    viewModelOf(::MainViewModel)
}

fun initKoin(context: Context) = GlobalContext.getKoinApplicationOrNull() ?: startKoin {
    androidLogger()
    androidContext(context)
    modules(mainModule)
}
