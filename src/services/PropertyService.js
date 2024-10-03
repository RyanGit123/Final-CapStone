import axios from 'axios';

export default {
  getProperties() {
    return axios.get('/properties');
  },
  searchProperties(filter) {
    return axios.get(`/properties?search=${filter}`);
  },

  addProperty(property) {
    return axios.post("/properties", property);
},
/**
 * Assigns a tenant to a property.
 *
 * @param {number} propertyId - The property ID.
 * @param {number} tenantId - The tenant ID.
 * @returns {Promise} A promise that resolves with the response from the server.
 */
assignTenant(propertyId, tenantId) {
    return axios.put(`/properties/${propertyId}/assign-tenant/${tenantId}`);
},
};