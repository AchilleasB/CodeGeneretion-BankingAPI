<template>
  <div class="unapproved-message">
    <h2>Your Account is Under Review</h2>
    <p>The bank is processing your account request.<br> We will contact you as soon as the request is resolved.</p>
    <img src="https://source.unsplash.com/random/400x200?banking" alt="Processing" />
  </div>
</template>

<style scoped>
.unapproved-message {
  text-align: center;
  padding: 20px;
  background-color: #f9f9f9;
  border: 1px solid #ddd;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  margin: 20px;
}

.unapproved-message h2 {
  font-size: 24px;
  color: #333;
  margin-bottom: 10px;
}

.unapproved-message p {
  font-size: 1.2em;
  color: #666;
  margin-bottom: 20px;
}

.unapproved-message img {
  margin-top: 20px;
  width: 100%;
  max-width: 800px;
  border-radius: 8px;
}
</style>