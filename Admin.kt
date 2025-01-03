import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.util.Scanner

class AdminApp {
    private val scanner = Scanner(System.`in`)
    private val connection: Connection

    init {
        val url = "jdb" +
                "c:mysql://localhost:3306/tiket" // Nama database diubah menjadi `tiket`
        val user = "root" // Ganti dengan username MySQL Anda
        val password = "" // Ganti dengan password MySQL Anda
        connection = DriverManager.getConnection(url, user, password)
        println("Koneksi ke database berhasil.")
    }

    fun start() {
        while (true) {
            println("\n=== SELAMAT DATANG DI PEMESANAN TIKET KAPAL FERY ===")
            println("1. Lihat Semua Tiket")
            println("2. Tambah Tiket")
            println("3. Edit Tiket")
            println("4. Hapus Tiket")
            println("5. Keluar")
            print("Pilih opsi: ")

            when (scanner.nextInt()) {
                1 -> viewTickets()
                2 -> addTicket()
                3 -> editTicket()
                4 -> deleteTicket()
                5 -> {
                    println("Keluar...")
                    connection.close()
                    return
                }
                else -> println("Pilihan tidak valid.")
            }
        }
    }

    private fun addTicket() {
        print("Masukkan rute: ")
        val rute = scanner.next()
        print("Masukkan waktu keberangkatan (yyyy-MM-dd HH:mm:ss): ")
        val departure_time = scanner.next()
        print("Masukkan harga: ")
        val price = scanner.next()
        print("Masukkan jumlah kursi tersedia: ")
        val available_seats = scanner.next()

        val query = "INSERT INTO tickets (rute, departure_time, price, available_seats) VALUES (?, ?, ?, ?)"
        val statement = connection.prepareStatement(query)
        statement.setString(1, rute)
        statement.setString(2, departure_time)
        statement.setString(3, price)
        statement.setString(4, available_seats)
        statement.executeUpdate()
        println("Tiket berhasil ditambahkan.")
    }



    private fun viewTickets() {
        val query = "SELECT * FROM tickets"
        val statement = connection.createStatement()
        val resultSet: ResultSet = statement.executeQuery(query)

        println("\n=== Semua Tiket ===")
        while (resultSet.next()) {
            println(
                "ID: ${resultSet.getInt("id")}, " +
                        "Rute: ${resultSet.getString("rute")}, " +
                        "Waktu: ${resultSet.getString("departure_time")}, " +
                        "Harga: ${resultSet.getString("price")}, " +
                        "Kursi Tersedia: ${resultSet.getString("available_seats")}"
            )
        }
    }

    private fun editTicket() {
        print("Masukkan ID tiket yang akan diedit: ")
        val id = scanner.nextInt()
        print("Masukkan rute baru: ")
        val rute = scanner.next()
        print("Masukkan waktu keberangkatan baru (yyyy-MM-dd HH:mm:ss): ")
        val departureTime = scanner.next()
        print("Masukkan harga baru: ")
        val price = scanner.next()
        print("Masukkan jumlah kursi tersedia baru: ")
        val seats = scanner.next()

        val query = "UPDATE tickets SET rute = ?, departure_time = ?, price = ?, available_seats = ? WHERE id = ?"
        val statement = connection.prepareStatement(query)
        statement.setString(1, rute)
        statement.setString(2, departureTime)
        statement.setString(3, price)
        statement.setString(4, seats)
        statement.setInt(5, id)
        statement.executeUpdate()
        println("Tiket berhasil diperbarui.")
    }

    private fun deleteTicket() {
        print("Masukkan ID tiket yang akan dihapus: ")
        val id = scanner.nextInt()

        val query = "DELETE FROM tickets WHERE id = ?"
        val statement = connection.prepareStatement(query)
        statement.setInt(1, id)
        statement.executeUpdate()
        println("Tiket berhasil dihapus.")
    }
}

fun main() {
    val app = AdminApp()
    app.start()
}
