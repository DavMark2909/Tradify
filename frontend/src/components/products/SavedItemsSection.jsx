import ProductCarousel from './ProductCarousel';
import ProductGrid from './ProductGrid';
import StatusMessage from '../common/StatusMessage';
import './SavedItemsSection.css';

export default function SavedItemsSection({ items, previewItems, status, viewAll, onToggleViewAll }) {
  const canViewAll = items.length > previewItems.length;

  return (
    <section className="saved-items-section">
      <div className="saved-items-section__header">
        <h2>Saved Items</h2>
        {canViewAll && (
          <button type="button" className="saved-items-section__toggle" onClick={onToggleViewAll}>
            {viewAll ? 'Show less' : 'View all'}
          </button>
        )}
      </div>
      {status === 'loading' && <StatusMessage tone="loading">Loading saved items…</StatusMessage>}
      {status === 'error' && <StatusMessage tone="error">Couldn't load saved items.</StatusMessage>}
      {status === 'success' && items.length === 0 && (
        <StatusMessage tone="empty">You haven't saved any items yet.</StatusMessage>
      )}
      {status === 'success' && items.length > 0 && (
        viewAll ? <ProductGrid products={items} /> : <ProductCarousel products={previewItems} />
      )}
    </section>
  );
}
