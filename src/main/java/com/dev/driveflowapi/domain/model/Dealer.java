package com.dev.driveflowapi.domain.model;

import com.dev.driveflowapi.domain.exception.DomainException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Dealer {

    private UUID id;
    private String corporateName;
    private String cnpj;
    private String zipCode;
    private String address;

    public Dealer(
            UUID id,
            String corporateName,
            String cnpj,
            String zipCode,
            String address
    ) {
        validateCorporateName(corporateName);
        validateCnpj(cnpj);
        validateZipCode(zipCode);
        validateAddress(address);

        this.id = id;
        this.corporateName = corporateName;
        this.cnpj = cnpj;
        this.zipCode = zipCode;
        this.address = address;

    }

    private void validateCorporateName(String corporateName) {

        if (corporateName == null || corporateName.isBlank()) {
            throw new DomainException(
                    "Corporate name cannot be empty."
            );
        }
    }

    private void validateCnpj(String cnpj) {

        if (cnpj == null || cnpj.isBlank()) {
            throw new DomainException(
                    "CNPJ cannot be empty."
            );
        }

        String normalizedCnpj =
                cnpj.replaceAll("[^a-zA-Z0-9]", "");

        if (normalizedCnpj.length() != 14) {
            throw new DomainException(
                    "CNPJ must contain 14 characters."
            );
        }
    }

    private void validateZipCode(String zipCode) {

        if (zipCode == null || zipCode.isBlank()) {
            throw new DomainException(
                    "Zip code cannot be empty."
            );
        }

        String normalizedZipCode =
                zipCode.replaceAll("\\D", "");

        if (normalizedZipCode.length() != 8) {
            throw new DomainException(
                    "Zip code must contain 8 digits."
            );
        }
    }

    private void validateAddress(String address) {

        if (address == null || address.isBlank()) {
            throw new DomainException(
                    "Address cannot be empty."
            );
        }
    }

    public void update(
            String corporateName,
            String cnpj,
            String zipCode,
            String address
    ) {
        validateCorporateName(corporateName);
        validateCnpj(cnpj);
        validateZipCode(zipCode);
        validateAddress(address);

        this.corporateName = corporateName;
        this.cnpj = cnpj;
        this.zipCode = zipCode;
        this.address = address;
    }

}
