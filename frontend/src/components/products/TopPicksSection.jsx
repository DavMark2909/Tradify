import ProductCarousel from './ProductCarousel';
import StatusMessage from '../common/StatusMessage';
import './TopPicksSection.css';

export default function TopPicksSection({ products, status }) {
  return (
    <section className="top-picks-section">
      <h2>Top Picks</h2>
      {status === 'loading' && <StatusMessage tone="loading">Loading top picks…</StatusMessage>}
      {status === 'error' && <StatusMessage tone="error">Couldn't load top picks.</StatusMessage>}
      {status === 'success' && products.length === 0 && (
        <StatusMessage tone="empty">No top picks right now.</StatusMessage>
      )}
      {status === 'success' && products.length > 0 && <ProductCarousel products={products} />}
    </section>
  );
}
