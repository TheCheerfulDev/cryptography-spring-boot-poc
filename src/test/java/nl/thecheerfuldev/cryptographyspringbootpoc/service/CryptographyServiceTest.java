package nl.thecheerfuldev.cryptographyspringbootpoc.service;

import nl.thecheerfuldev.cryptographyspringbootpoc.repository.CryptoKeyRepository;
import nl.thecheerfuldev.cryptographyspringbootpoc.repository.PasswordSaltPair;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CryptographyServiceTest {

    @InjectMocks
    private CryptographyService sut;
    @Mock
    private CryptoKeyRepository cryptoKeyRepositoryMock;
    @Captor
    private ArgumentCaptor<String> passwordSaltPairCaptor;

    @Test
    @DisplayName("""
            GIVEN a userId and inputText
            WHEN the inputText is encrypted for userId 1001
            AND the encrypted inputText is decrypted for userId 1001 with the same password and key
            THEN the inputText and de decrypted inputText are the same
            """)
    void encryptionDecryptionTest() {
        // given
        final int userId = 1001;
        final String inputText = """
                {
                    "name" : "Mark Hendriks",
                    "Company" : "Ordina JTech",
                    "Function" : "Software Architect"
                }""";
        this.sut.generateKeyPasswordPairForUserId(userId);
        verify(this.cryptoKeyRepositoryMock).setKeyPasswordPairForUserId(ArgumentMatchers.anyInt(), this.passwordSaltPairCaptor.capture(), this.passwordSaltPairCaptor.capture());

        // when
        Mockito.when(this.cryptoKeyRepositoryMock.getKeyForUserId(userId))
                .thenReturn(Optional.ofNullable(PasswordSaltPair.builder()
                        .salt(this.passwordSaltPairCaptor.getAllValues().get(0))
                        .password(this.passwordSaltPairCaptor.getAllValues().get(1))
                        .build()));

        final String encryptedText = this.sut.encryptTextForUserId(inputText, userId);
        final String decryptedText = this.sut.decryptTextForUserId(encryptedText, userId);

        // then
        assertThat(decryptedText).isEqualTo(inputText);

        System.out.println(encryptedText);
        System.out.println(decryptedText);

    }

}