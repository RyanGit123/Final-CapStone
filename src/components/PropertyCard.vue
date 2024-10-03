<template>
  <Card class="property-card">
    <template #header>
      <div class="image-container">
        <img :alt="`Image of ${property.address}`" :src="propertyImage" />
      </div>
    </template>
    <template #title>{{ property.fullAddress }}</template>
    <template #subtitle>
      {{ property.roomCount }} Rooms | {{ property.bathroomCount }} Bathrooms | {{ property.sqFeet }} sq ft
    </template>
    <template #content>
      <p class="m-0">
        Price: ${{ property.price }} <br />
        Availability: {{ property.status ? 'Available' : 'Not Available' }}
      </p>
    </template>
    <template #footer>
      <div class="flex gap-4 mt-1">
        <Button label="Details" class="w-full" />
      </div>
    </template>
  </Card>
</template>

<script setup>
import { defineProps, computed } from 'vue';
import Card from 'primevue/card';
import Button from 'primevue/button';

const props = defineProps({
  property: {
    type: Object,
    required: true
  }
});

const randomImages = [
  '/stockapap.jpg',
  '/stockapap2.jpg',
  '/stockhome.jpg',
  '/stockhome2.jpg',
];

const propertyImage = computed(() => {
  return props.property.image || randomImages[Math.floor(Math.random() * randomImages.length)];
});

console.log('Property:', props.property); // Debugging: Check if property prop is received correctly
</script>

<style>
.property-card {
  width: 100%;
  height: 500px;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.image-container {
  height: 100%;
  width: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
}

.image-container img {
  max-width: 100%;
  max-height: 250px;
  object-fit: cover;
}
</style>
