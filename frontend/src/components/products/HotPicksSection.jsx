import ProductGrid from './ProductGrid';
import StatusMessage from '../common/StatusMessage';
import './HotPicksSection.css';

export default function HotPicksSection({ products, status }) {
  return (
    <section className="hot-picks-section">
      <h2>Hot Picks</h2>
      {status === 'loading' && <StatusMessage tone="loading">Loading hot picks…</StatusMessage>}
      {status === 'error' && <StatusMessage tone="error">Couldn't load hot picks.</StatusMessage>}
      {status === 'success' && products.length === 0 && (
        <StatusMessage tone="empty">No hot picks match your search.</StatusMessage>
      )}
      {status === 'success' && products.length > 0 && <ProductGrid products={products} />}
    </section>
  );
}
