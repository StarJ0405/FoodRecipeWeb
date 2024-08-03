/** @type {import('tailwindcss').Config} */
module.exports = {
   content: ["../templates/**/*.html"],
  darkMode: false,
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

