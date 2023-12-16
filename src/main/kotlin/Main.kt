import kotlin.random.Random

class Philosopher(val name: String, val leftFork: Fork, val rightFork: Fork) : Runnable {
    override fun run() {
        if (!leftFork.isTaken) {
            leftFork.take()
            if (!rightFork.isTaken) {
                rightFork.take()
                println("$name is dining")
                rightFork.put()
            } else {
                leftFork.put()
            }
        }
    }
}

class Fork(val name: String) {
    @Volatile
    var isTaken = false
    fun take() {
        isTaken = true
    }

    fun put() {
        isTaken = false
    }
}

fun main() {
    val philosophers = listOf("Аристотель", "Платон", "Сократ", "Декарт", "Кант")
    val forks = mutableListOf<Fork>()
    for (i in 1..philosophers.size) {
        forks.add(Fork("Fork $i"))
    }
    val threads = mutableListOf<Thread>()
    val randomStart = Random.nextInt(philosophers.size) // Randomly choose starting philosopher
    for (i in 0 until philosophers.size) {
        val leftFork = forks[i]
        val rightFork = if (i == philosophers.size - 1) {
            forks[0]
        } else {
            forks[i + 1]
        }
        val philosopher = Philosopher(philosophers[(randomStart + i) % philosophers.size], leftFork, rightFork)
        threads.add(Thread(philosopher))
    }

    threads.forEach { it.start() }
    threads.forEach { it.join() }
}