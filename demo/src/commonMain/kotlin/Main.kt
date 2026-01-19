// Can't have a package for kotlin/native main() function

import com.stilllynnthecloset.libmpd.LibMpd
import com.stilllynnthecloset.libmpd.protocol.MpdCommand
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

public fun main(): Unit = runBlocking {
    val connection = LibMpd.getConnection(debug = false)

    println(connection.runCommand(MpdCommand.Status()))
    println(connection.runCommand(MpdCommand.Stats()))
    println(connection.runCommand(MpdCommand.CurrentSong()))
    println(connection.runCommand(MpdCommand.Commands()))

    while (true) {
        println(connection.runCommand(MpdCommand.Status()))
        delay(1000)
    }

//    val result5 = connection.runCommandList(MpdCommandList(
//        commands = listOf(
//            MpdCommand.Status(),
//            MpdCommand.Stats(),
//            MpdCommand.CurrentSong(),
//        ),
//        listOk = true,
//    ))
//    println(result5.results.joinToString(separator = "\n"))

//    val result6 = connection.runCommandList(
//        MpdCommand.Status(),
//        MpdCommand.Stats(),
//        MpdCommand.CurrentSong(),
//        MpdCommand.Commands(),
//    )
//    println(result6.joinToString(separator = "\n"))
}
