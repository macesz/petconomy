import React from 'react'
import { badges } from '../utils'

function BadgeContainer() {
  return (
    <div className='flex h-full gap-4 p-2'>
      {/* Példa profilkép */}
      <img
        className='rounded object-cover'
        src="https://img.daisyui.com/images/stock/photo-1534528741775-53994a69daeb.webp"
        alt="Profile"
      />

      {/* Badge-ek megjelenítése */}
      <div className='flex gap-4 items-center'>
        {badges.map((badge, index) => (
          <img
            key={index}
            src={badge.picture}
            title={`Badge ${index + 1}`}
            className={`h-full ${!badge.metReg && `opacity-50`}`}
          />
        ))}
      </div>
    </div>
  )
}

export default BadgeContainer
