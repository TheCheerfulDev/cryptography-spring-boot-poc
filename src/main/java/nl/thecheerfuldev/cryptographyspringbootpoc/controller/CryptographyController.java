package nl.thecheerfuldev.cryptographyspringbootpoc.controller;

import nl.thecheerfuldev.cryptographyspringbootpoc.service.CryptographyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/crypto")
public class CryptographyController {

    private final CryptographyService cryptographyService;

    public CryptographyController(CryptographyService cryptographyService) {
        this.cryptographyService = cryptographyService;
    }

    @GetMapping("/create/{userId}")
    public ResponseEntity<Void> createKeyForUser(@PathVariable("userId") final int userId) {
        this.cryptographyService.generateKeyPasswordPairForUserId(userId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteKeyForUser(@PathVariable("userId") final int userId) {
        this.cryptographyService.deletePasswordSaltPairForUserId(userId);
        return ResponseEntity.ok().build();

    }


}
