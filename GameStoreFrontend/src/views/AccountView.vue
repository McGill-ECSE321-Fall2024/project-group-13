<template>
    <div class="address">
    <p class="title">Address</p>
    <form class="form">
        <div class="input">
            <label for="street">Street</label>
            <input type="text" name="street" id="street" placeholder="" required>
        </div>
        <div class="input">
            <label for="number">Street No.</label>
            <input type="number" name="number" id="number" placeholder="" step="1" required>
        </div>
        <div class="input">
            <label for="apartment-number">Apartment No.</label>
            <input type="number" name="apartment-number" id="apartment-number" step="1" placeholder="">
        </div>
        <div class="input">
            <label for="city">City</label>
            <input type="text" name="city" id="city" placeholder="" required>
        </div>
        <div class="input">
            <label for="state-province">State/Province</label>
            <input type="text" name="state-province" id="state-province" placeholder="" required>
        </div>
        <div class="input">
            <label for="phone-number">Phone Number</label>
            <input type="text" name="phone-number" id="phone-number" placeholder="" required>
        </div>
        <div class="input">
            <label for="country">Country</label>
            <input type="text" name="country" id="country" placeholder="" required>
        </div>
        <div class="input">
            <label for="postal">Postal Code</label>
            <input type="text" name="postal" id="postal" placeholder="" required>
        </div>
        <button class="button">Update Address</button>
    </form>
    </div>
    <div class="payment-info">
    <p class="title">Payment Information</p>
    <form class="form">
        <div class="input">
            <label for="name">Cardholder Name</label>
            <input type="text" name="name" id="name" placeholder="" required>
        </div>
        <div class="input">
            <label for="card-number">Card Number</label>
            <input type="text" name="card-number" id="card-number" placeholder="" required>
        </div>
        <div class="input">
            <label for="expiry-date">Expiry Date</label>
            <input type="date" name="expiry-date" id="expiry-date" placeholder="" required>
        </div>

        <div class="input">
            <label for="cvv">CVV Code</label>
            <input type="number" name="cvv" id="cvv" placeholder="" required>
        </div>
        <button class="button">Update Payment Information</button>
    </form>
    </div>
</template>

<script>
export default {
    name: 'AccountView',
}
</script>

<style scoped>
.payment-info, .address {
  width: 500px;
  margin-left: auto;
  margin-right: auto;
  border-radius: 0.75rem;
  background-color: #1e1e1e;
  padding: 2rem;
  color: rgba(243, 244, 246, 1);
}

.payment-info {
    margin-top: 20px;
}

.title {
  text-align: center;
  font-size: 1.5rem;
  line-height: 2rem;
  font-weight: 700;
  color: rgba(243, 244, 246, 1);
}

.form {
  margin-top: 1.5rem;
}

.input {
  margin-top: 0.25rem;
  font-size: 0.875rem;
  line-height: 1.25rem;
}

.input label {
  display: block;
  color: rgba(156, 163, 175, 1);
  margin-bottom: 4px;
  margin-top: 20px;
}

.input input {
  width: 100%;
  border-radius: 0.375rem;
  border: 1px solid rgba(55, 65, 81, 1);
  outline: 0;
  background-color: #1e1e1e;
  padding: 0.75rem 1rem;
  color: rgba(243, 244, 246, 1);
}

.input input:focus {
  border-color: rgba(167, 139, 250);
}

.signup a {
  color: rgba(243, 244, 246, 1);
  text-decoration: none;
  font-size: 14px;
}

.signup a:hover {
  text-decoration: underline rgba(167, 139, 250, 1);
}

.button {
  margin-top: 10px;
  display: block;
  width: 100%;
  background-color: rgba(167, 139, 250, 1);
  padding: 0.75rem;
  text-align: center;
  color: rgba(17, 24, 39, 1);
  border: none;
  border-radius: 0.375rem;
  font-weight: 600;
  cursor: pointer;
}

.sign-in:hover {
  text-decoration: underline rgba(55, 65, 81, 1);
}
</style>