package eu.withoutaname.withoutabot

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import com.zaxxer.hikari.pool.HikariPool.PoolInitializationException
import eu.withoutaname.withoutabot.tables.InfoCommandActions
import eu.withoutaname.withoutabot.tables.InfoCommands
import eu.withoutaname.withoutabot.tables.Statuses
import kotlinx.coroutines.delay
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

context(LoggingContext) suspend
fun initDB() {
    connectToDB()
    transaction {
        SchemaUtils.createMissingTablesAndColumns(InfoCommands, InfoCommandActions, Statuses)
    }
}

context(LoggingContext) suspend
fun connectToDB() {
    val config = HikariConfig().apply {
        jdbcUrl = "jdbc:mysql://db/withoutabot"
        driverClassName = "com.mysql.cj.jdbc.Driver"
        username = "withoutabot"
        password = "password"
        minimumIdle = 1
        maximumPoolSize = 10
    }
    try {
        val dataSource = HikariDataSource(config)
        Database.connect(dataSource)
    } catch (e: PoolInitializationException) {
        log.warn("Failed to connect to database, retrying in 5 seconds")
        delay(5000)
        connectToDB()
    }
}