import axios from "axios";

export default {    
    assignTenant(rentalAgreement) {
        return axios.post('/rental_agreements', rentalAgreement);
    },

    getRentalAgreementsByPropertyId(id) {
        return axios.get(`/rental_agreements/properties/${id}`);
    },

    getRentalAgreementsByLandlordId(id){     
        return axios.get(`/rental_agreements/landlords/${id}`);
    },

    getRentalAgreementsByRenterId(id){        
        return axios.get(`/rental_agreements/renters/${id}`);
    }

};