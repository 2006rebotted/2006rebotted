package plugin.commands



class CommandParser(command: String) {
	
	private val arguments : List<String>
	private var argumentIndex = 0

	init {
		arguments = command.split(" ")
	}

	val command: String
		get() {
			return arguments[0]
		}

	@JvmOverloads fun hasNext(length: Int = 1): Boolean {
		return argumentIndex + length < arguments.size
	}

	@Throws(NumberFormatException::class)
	fun nextByte(): Byte {
		return nextString()!!.toByte()
	}

	@Throws(NumberFormatException::class)
	fun nextDouble(): Double {
		return nextString()!!.toDouble()
	}

	@Throws(NumberFormatException::class)
	fun nextInt(): Int {
		return nextString()!!.toInt()
	}

	@Throws(NumberFormatException::class)
	fun nextLong(): Long {
		return nextString()!!.toLong()
	}

	@Throws(NumberFormatException::class)
	fun nextShort(): Short {
		return nextString()!!.toShort()
	}

	@Throws(ArrayIndexOutOfBoundsException::class)
	fun nextString(): String? {
		if (argumentIndex + 1 >= arguments.size) {
			throw ArrayIndexOutOfBoundsException("The next argument does not exist. [Size: " + arguments.size + ", Attempted: " + (argumentIndex + 1) + "]")
		}
		return arguments[++argumentIndex]
	}
		
	override fun toString(): String {
		var string = ""
		for (argument in arguments) {
			string += argument + " "
		}
		return string.trim()
	}
}