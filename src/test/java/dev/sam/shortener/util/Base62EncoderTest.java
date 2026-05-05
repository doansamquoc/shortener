package dev.sam.shortener.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class Base62EncoderTest {

	@Test
	void encode() {
		long input = 3; // The index of 'd' in ALPHABET
		String code = Base62Encoder.encode(input);
		Assertions.assertEquals("d", code);
	}

	@Test
	void decode() {
		String input = "d"; // The code wanna decode
		long code = Base62Encoder.decode(input);
		Assertions.assertEquals(3, code); // 3 is the index of 'd'
	}
}