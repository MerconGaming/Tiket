import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.util.Scanner

class UserApp {
    private val scanner = Scanner(System.`in`)
    private val connection: Connection

    init {
        val url = "jdbc:mysql://localhost:3306/tiket" // Pastikan nama database "tiket"
        val user = "root" // Ubah sesuai dengan user MySQL Anda
        val password = "" // Ganti sesuai dengan password MySQL Anda
        connection = DriverManager.getConnection(url, user, password)
        println("Koneksi ke database berhasil.")
    }

    fun start() {
        while (true) {
            println("\n=== Menu Pengguna ===")
            println("1. Lihat Tiket")
            println("2. Pesan Tiket")
            println("3. Keluar")
            print("Pilih opsi: ")

            when (scanner.nextInt()) {
                1 -> viewTickets()
                2 -> bookTicket()
                3 -> {
                    println("Keluar...")
                    connection.close()
                    return
                }
                else -> println("Pilihan tidak valid.")
            }
        }
    }

    private fun viewTickets() {
        val query = "SELECT * FROM tickets WHERE available_seats > 0"
        val statement = connection.createStatement()
        val resultSet: ResultSet = statement.executeQuery(query)

        println("\n=== Tiket Tersedia ===")
        while (resultSet.next()) {
            println("ID: ${resultSet.getInt("id")}, Rute: ${resultSet.getString("rute")}, Waktu: ${resultSet.getString("departure_time")}, Harga: ${resultSet.getString("price")}, Kursi Tersedia: ${resultSet.getInt("available_seats")}")
        }
    }

    private fun bookTicket() {
        print("Masukkan ID tiket yang ingin dipesan: ")
        val ticketId = scanner.nextInt()
        print("Masukkan nama Anda: ")
        val userName = scanner.next()

        val checkQuery = "SELECT available_seats FROM tickets WHERE id = ?"
        val checkStatement = connection.prepareStatement(checkQuery)
        checkStatement.setInt(1, ticketId)
        val resultSet = checkStatement.executeQuery()

        if (resultSet.next() && resultSet.getInt("available_seats") > 0) {
            // Proses pemesanan tiket
            val bookingQuery = "INSERT INTO bookings (ticket_id, user_name) VALUES (?, ?)" // Menggunakan tabel bookings
            val bookingStatement = connection.prepareStatement(bookingQuery)
            bookingStatement.setInt(1, ticketId)
            bookingStatement.setString(2, userName)
            bookingStatement.executeUpdate()

            // Update kursi yang tersedia
            val updateQuery = "UPDATE tickets SET available_seats = available_seats - 1 WHERE id = ?"
            val updateStatement = connection.prepareStatement(updateQuery)
            updateStatement.setInt(1, ticketId)
            updateStatement.executeUpdate()

            println("Tiket berhasil dipesan.")
        } else {
            println("Tiket tidak tersedia.")
        }
    }
}

fun main() {
    val app = UserApp()
    app.start()
}
