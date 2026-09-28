import { useRef } from 'react';
import ProductCard from './ProductCard';
import { ChevronLeftIcon, ChevronRightIcon } from '../icons/Icons';
import './ProductCarousel.css';

export default function ProductCarousel({ products }) {
  const trackRef = useRef(null);

  const scrollByAmount = (direction) => {
    const track = trackRef.current;
    if (!track) return;
    track.scrollBy({ left: direction * track.clientWidth * 0.9, behavior: 'smooth' });
  };

  return (
    <div className="product-carousel">
      <button
        type="button"
        className="product-carousel__nav product-carousel__nav--prev"
        onClick={() => scrollByAmount(-1)}
        aria-label="Scroll left"
      >
        <ChevronLeftIcon size={18} />
      </button>

      <div className="product-carousel__track" ref={trackRef}>
        {products.map((product) => (
          <div className="product-carousel__item" key={product.id}>
            <ProductCard product={product} />
          </div>
        ))}
      </div>

      <button
        type="button"
        className="product-carousel__nav product-carousel__nav--next"
        onClick={() => scrollByAmount(1)}
        aria-label="Scroll right"
      >
        <ChevronRightIcon size={18} />
      </button>
    </div>
  );
}
