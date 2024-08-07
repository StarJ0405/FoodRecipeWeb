/** @type {import('tailwindcss').Config} */
module.exports = {
   content: ["../templates/**/*.html"],
  media: false,
  theme: {
    extend: {},
  },
  daisyui: {
    themes: ["light"],
  },
  plugins: [
      require('daisyui')
  ],
}

